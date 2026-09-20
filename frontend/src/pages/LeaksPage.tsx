import { useEffect, useState } from "react";
import { EmptyState } from "../components/common/EmptyState";
import { RiskBadge } from "../components/common/RiskBadge";
import { StatCard } from "../components/common/StatCard";
import { StatusBadge } from "../components/common/StatusBadge";
import { LeakLevel } from "../constants/LeakLevel";
import { ReporterType, ReporterTypeText } from "../constants/ReporterType";
import { VerifyStatusText } from "../constants/VerifyStatus";
import { useLeakSeverity } from "../hooks/useLeakSeverity";
import { useInspectionPointStore } from "../stores/InspectionPointStore";
import { useLeakReportStore } from "../stores/LeakReportStore";
import { useRepairOrderStore } from "../stores/RepairOrderStore";
import type { LeakReportView } from "../types/LeakReportView";
import { formatDate, formatLeakLevel, formatOverdue } from "../utils/formatters";

type Notice = { kind: "ok" | "err"; text: string } | null;

export function LeaksPage() {
  const { rows, loading, load, create, verify } = useLeakReportStore();
  const { rows: points, load: loadPoints } = useInspectionPointStore();
  const { crews, loadCrews, dispatch } = useRepairOrderStore();
  const severity = useLeakSeverity(rows);

  const [notice, setNotice] = useState<Notice>(null);
  const [reportOpen, setReportOpen] = useState(false);
  const [verifyTarget, setVerifyTarget] = useState<LeakReportView | null>(null);
  const [dispatchTarget, setDispatchTarget] = useState<LeakReportView | null>(null);

  useEffect(() => {
    load();
    loadPoints();
    loadCrews();
  }, [load, loadPoints, loadCrews]);

  async function run(action: () => Promise<string | null>, okText: string) {
    const err = await action();
    setNotice(err ? { kind: "err", text: err } : { kind: "ok", text: okText });
    return err;
  }

  return (
    <main className="page">
      <section className="page-head">
        <div>
          <p className="eyebrow">water-leak</p>
          <h1>漏损核查</h1>
        </div>
        <button className="btn primary" onClick={() => setReportOpen(true)}>上报漏损</button>
      </section>

      <p className="hint">核查排序：漏损等级 BURST→TRACE → 管段风险高→低 → 同分看巡检超期（超期久的在前）。BURST 未核实不能派工。</p>

      {notice && <p className={"notice " + notice.kind}>{notice.text}</p>}

      <section className="metrics">
        <StatCard label="漏损报告" value={severity.total} />
        <StatCard label="BURST 级" value={severity.byLevel.BURST ?? 0} />
        <StatCard label="待核实" value={severity.pendingVerify} />
        <StatCard label="巡检超期点" value={severity.overdue} />
      </section>

      <section className="panel wide">
        {rows.length === 0 && !loading ? <EmptyState title="暂无漏损报告" /> : (
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th><th>等级</th><th>管段</th><th>风险</th><th>巡检点</th><th>巡检超期</th>
                <th>上报时间</th><th>上报人</th><th>核实状态</th><th>阻塞原因</th><th>操作</th>
              </tr>
            </thead>
            <tbody>
              {rows.map((row) => (
                <tr key={row.id}>
                  <td>{row.id}</td>
                  <td><StatusBadge value={row.leak_level} text={formatLeakLevel(row.leak_level)} /></td>
                  <td>{row.segment_code}</td>
                  <td><RiskBadge value={row.risk_level} /></td>
                  <td>{row.point_code}</td>
                  <td className={row.overdue_days > 0 ? "overdue" : "ok-text"}>{formatOverdue(row.overdue_days)}</td>
                  <td>{formatDate(row.reported_at)}</td>
                  <td>{ReporterTypeText[row.reporter_type as keyof typeof ReporterTypeText] ?? row.reporter_type}</td>
                  <td><StatusBadge value={row.verify_status} text={VerifyStatusText[row.verify_status as keyof typeof VerifyStatusText] ?? row.verify_status} /></td>
                  <td>{row.block_reason ? <span className="block-reason">{row.block_reason}</span> : "—"}</td>
                  <td className="actions">
                    {row.verify_status === "PENDING" && (
                      <button className="btn ghost" onClick={() => setVerifyTarget(row)}>核实</button>
                    )}
                    <button className="btn ghost" onClick={() => setDispatchTarget(row)}>派工</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>

      {reportOpen && (
        <ReportModal
          points={points.map((p) => ({ id: p.id, point_code: p.point_code }))}
          onClose={() => setReportOpen(false)}
          onSubmit={async (payload) => {
            const err = await run(() => create(payload), "上报成功，等待核实");
            if (!err) setReportOpen(false);
            return err;
          }}
        />
      )}

      {verifyTarget && (
        <VerifyModal
          report={verifyTarget}
          onClose={() => setVerifyTarget(null)}
          onSubmit={async (result) => {
            const err = await run(() => verify(verifyTarget.id, result), result === "VERIFIED" ? "已核实，可派工" : "已驳回为误报");
            if (!err) setVerifyTarget(null);
            return err;
          }}
        />
      )}

      {dispatchTarget && (
        <DispatchModal
          report={dispatchTarget}
          crews={crews}
          onClose={() => setDispatchTarget(null)}
          onSubmit={async (payload) => {
            const err = await run(() => dispatch(payload), "派工成功");
            if (!err) setDispatchTarget(null);
            return err;
          }}
        />
      )}
    </main>
  );
}

function Modal({ title, children, onClose }: { title: string; children: React.ReactNode; onClose: () => void }) {
  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-head"><h2>{title}</h2><button className="btn ghost" onClick={onClose}>关闭</button></div>
        {children}
      </div>
    </div>
  );
}

function ReportModal({ points, onClose, onSubmit }: {
  points: { id: number; point_code: string }[];
  onClose: () => void;
  onSubmit: (payload: { reporterType: string; pointId: number; leakLevel: string; description: string; photoUrl?: string }) => Promise<string | null>;
}) {
  const [reporterType, setReporterType] = useState<string>(ReporterType[0]);
  const [pointId, setPointId] = useState<number>(points[0]?.id ?? 0);
  const [leakLevel, setLeakLevel] = useState<string>(LeakLevel[0]);
  const [description, setDescription] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [busy, setBusy] = useState(false);

  async function submit() {
    setBusy(true);
    const err = await onSubmit({ reporterType, pointId, leakLevel, description });
    setBusy(false);
    setError(err);
  }

  return (
    <Modal title="上报漏损" onClose={onClose}>
      <label className="form-row"><span>上报人</span>
        <select value={reporterType} onChange={(e) => setReporterType(e.target.value)}>
          {ReporterType.map((t) => <option key={t} value={t}>{ReporterTypeText[t]}</option>)}
        </select>
      </label>
      <label className="form-row"><span>巡检点</span>
        <select value={pointId} onChange={(e) => setPointId(Number(e.target.value))}>
          {points.map((p) => <option key={p.id} value={p.id}>{p.point_code}</option>)}
        </select>
      </label>
      <label className="form-row"><span>漏损等级</span>
        <select value={leakLevel} onChange={(e) => setLeakLevel(e.target.value)}>
          {LeakLevel.map((l) => <option key={l} value={l}>{formatLeakLevel(l)}（{l}）</option>)}
        </select>
      </label>
      <label className="form-row"><span>描述</span>
        <textarea value={description} onChange={(e) => setDescription(e.target.value)} rows={3} placeholder="现场情况描述" />
      </label>
      {error && <p className="notice err">{error}</p>}
      <div className="modal-actions">
        <button className="btn primary" disabled={busy || !pointId} onClick={submit}>{busy ? "提交中…" : "提交上报"}</button>
      </div>
    </Modal>
  );
}

function VerifyModal({ report, onClose, onSubmit }: {
  report: LeakReportView;
  onClose: () => void;
  onSubmit: (result: "VERIFIED" | "REJECTED") => Promise<string | null>;
}) {
  const [error, setError] = useState<string | null>(null);
  const [busy, setBusy] = useState(false);

  async function submit(result: "VERIFIED" | "REJECTED") {
    setBusy(true);
    const err = await onSubmit(result);
    setBusy(false);
    setError(err);
  }

  return (
    <Modal title={`核实漏损报告 #${report.id}`} onClose={onClose}>
      <p className="hint">{report.point_code} / {report.segment_code} / {formatLeakLevel(report.leak_level)}：{report.description}</p>
      {error && <p className="notice err">{error}</p>}
      <div className="modal-actions">
        <button className="btn primary" disabled={busy} onClick={() => submit("VERIFIED")}>核实通过</button>
        <button className="btn danger" disabled={busy} onClick={() => submit("REJECTED")}>误报驳回</button>
      </div>
    </Modal>
  );
}

function DispatchModal({ report, crews, onClose, onSubmit }: {
  report: LeakReportView;
  crews: { id: number; crew_code: string; crew_name: string }[];
  onClose: () => void;
  onSubmit: (payload: { leakReportId: number; crewId: number; priority: string; dispatchDate: string }) => Promise<string | null>;
}) {
  const [crewId, setCrewId] = useState<number>(crews[0]?.id ?? 0);
  const [priority, setPriority] = useState("P2");
  const [dispatchDate, setDispatchDate] = useState(() => new Date().toISOString().slice(0, 10));
  const [error, setError] = useState<string | null>(null);
  const [busy, setBusy] = useState(false);

  async function submit() {
    setBusy(true);
    const err = await onSubmit({ leakReportId: report.id, crewId, priority, dispatchDate });
    setBusy(false);
    setError(err);
  }

  return (
    <Modal title={`派工：漏损报告 #${report.id}（${formatLeakLevel(report.leak_level)}）`} onClose={onClose}>
      {report.leak_level === "BURST" && report.verify_status !== "VERIFIED" && (
        <p className="notice err">BURST 级漏损未核实，不能派工，请先在列表中完成核实。</p>
      )}
      <label className="form-row"><span>维修队</span>
        <select value={crewId} onChange={(e) => setCrewId(Number(e.target.value))}>
          {crews.map((c) => <option key={c.id} value={c.id}>{c.crew_code} {c.crew_name}</option>)}
        </select>
      </label>
      <label className="form-row"><span>优先级</span>
        <select value={priority} onChange={(e) => setPriority(e.target.value)}>
          <option value="P1">P1 紧急</option>
          <option value="P2">P2 一般</option>
          <option value="P3">P3 计划</option>
        </select>
      </label>
      <label className="form-row"><span>派工日期</span>
        <input type="date" value={dispatchDate} onChange={(e) => setDispatchDate(e.target.value)} />
      </label>
      <p className="hint">同一维修队同一天只能有一张未关闭维修单，冲突时本次派工将被拒绝。</p>
      {error && <p className="notice err">{error}</p>}
      <div className="modal-actions">
        <button className="btn primary" disabled={busy || !crewId} onClick={submit}>{busy ? "派工中…" : "确认派工"}</button>
      </div>
    </Modal>
  );
}
