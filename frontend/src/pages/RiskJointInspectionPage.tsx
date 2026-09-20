import { useEffect, useMemo, useState } from "react";
import { useRiskJointInspectionStore } from "../stores/RiskJointInspectionStore";
import { StatCard } from "../components/common/StatCard";
import { StatusBadge } from "../components/common/StatusBadge";
import { RiskBadge } from "../components/common/RiskBadge";
import { EmptyState } from "../components/common/EmptyState";
import { LeakLevel } from "../constants/LeakLevel";
import { VerifyStatusText } from "../constants/VerifyStatus";
import { formatDate, formatRisk } from "../utils/formatters";
import type { RepairOrder } from "../types/RepairOrder";
import type { MaterialLineInput } from "../types/RepairFlow";

const today = () => new Date().toISOString().slice(0, 10);

function ReportPanel() {
  const points = useRiskJointInspectionStore((s) => s.points);
  const report = useRiskJointInspectionStore((s) => s.report);
  const [pointId, setPointId] = useState<number>(0);
  const [leakLevel, setLeakLevel] = useState<string>("MINOR");
  const [reporterType, setReporterType] = useState<string>("RESIDENT");
  const [description, setDescription] = useState("");

  useEffect(() => {
    if (pointId === 0 && points.length > 0) setPointId(points[0].id);
  }, [points, pointId]);

  const submit = async () => {
    if (!pointId) return;
    const ok = await report({ pointId, leakLevel, reporterType, description });
    if (ok) setDescription("");
  };

  return (
    <div className="panel">
      <h2>漏损上报</h2>
      <div className="form-grid">
        <label>巡检点
          <select value={pointId || ""} onChange={(e) => setPointId(Number(e.target.value))}>
            {points.map((p) => <option key={p.id} value={p.id}>{p.point_code} · {p.address_desc}</option>)}
          </select>
        </label>
        <label>漏损级别
          <select value={leakLevel} onChange={(e) => setLeakLevel(e.target.value)}>
            {LeakLevel.map((l) => <option key={l} value={l}>{l}</option>)}
          </select>
        </label>
        <label>上报人
          <select value={reporterType} onChange={(e) => setReporterType(e.target.value)}>
            <option value="RESIDENT">居民</option>
            <option value="INSPECTOR">巡检员</option>
          </select>
        </label>
        <label className="span-2">描述
          <input value={description} onChange={(e) => setDescription(e.target.value)} placeholder="现场漏损情况" />
        </label>
        <div className="form-actions">
          <button className="primary" onClick={submit} disabled={!pointId}>提交上报</button>
        </div>
      </div>
    </div>
  );
}

function DispatchCell({ reportId, disabled, reason }: { reportId: number; disabled: boolean; reason: string | null }) {
  const crews = useRiskJointInspectionStore((s) => s.crews);
  const dispatch = useRiskJointInspectionStore((s) => s.dispatch);
  const [crewId, setCrewId] = useState<number>(0);
  const [workDate, setWorkDate] = useState<string>(today());

  useEffect(() => {
    if (crewId === 0 && crews.length > 0) setCrewId(crews[0].id);
  }, [crews, crewId]);

  if (disabled) return <span className="blocked" title={reason ?? ""}>{reason ?? "不可派工"}</span>;
  return (
    <div className="dispatch-cell">
      <select value={crewId || ""} onChange={(e) => setCrewId(Number(e.target.value))}>
        {crews.map((c) => <option key={c.id} value={c.id}>{c.crewName}</option>)}
      </select>
      <input type="date" value={workDate} onChange={(e) => setWorkDate(e.target.value)} />
      <button className="primary" disabled={!crewId || !workDate}
        onClick={() => dispatch({ leakReportId: reportId, crewId, workDate })}>派工</button>
    </div>
  );
}

function QueuePanel() {
  const queue = useRiskJointInspectionStore((s) => s.queue);
  const verify = useRiskJointInspectionStore((s) => s.verify);

  return (
    <div className="panel wide">
      <h2>漏损核查队列（BURST→TRACE · 管段风险高→低 · 同分看巡检超期）</h2>
      {queue.length === 0 ? <EmptyState title="暂无待核查漏损报告" /> : (
        <table className="data-table">
          <thead>
            <tr>
              <th>#</th><th>级别</th><th>管段 / 风险</th><th>巡检超期</th>
              <th>上报时间</th><th>核实</th><th>派工</th>
            </tr>
          </thead>
          <tbody>
            {queue.map((row, idx) => (
              <tr key={row.report_id}>
                <td>{idx + 1}</td>
                <td><StatusBadge value={row.leak_level} /></td>
                <td>
                  <div className="cell-stack">
                    <strong>{row.segment_code ?? "-"}</strong>
                    <span className="muted">{row.district ?? ""}</span>
                    {row.risk_level && <RiskBadge title={`风险 ${formatRisk(row.risk_level)}`} value={row.risk_level} />}
                  </div>
                </td>
                <td>
                  {row.overdue_days > 0
                    ? <span className="overdue">超期 {row.overdue_days} 天</span>
                    : <span className="muted">未超期</span>}
                  <div className="muted small">{row.check_frequency ?? ""}</div>
                </td>
                <td className="muted">{row.reported_at ? formatDate(row.reported_at) : "-"}</td>
                <td>
                  <div className="cell-stack">
                    <StatusBadge value={row.verify_status} />
                    {row.verify_status === "PENDING" && (
                      <div className="row-actions">
                        <button onClick={() => verify(row.report_id, "VERIFIED")}>核实通过</button>
                        <button onClick={() => verify(row.report_id, "REJECTED")}>驳回</button>
                      </div>
                    )}
                    <span className="muted small">{VerifyStatusText[row.verify_status as keyof typeof VerifyStatusText] ?? row.verify_status}</span>
                  </div>
                </td>
                <td>
                  <DispatchCell reportId={row.report_id} disabled={!row.dispatchable} reason={row.blocked_reason} />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

function AcceptModal({ order, onClose }: { order: RepairOrder; onClose: () => void }) {
  const stocks = useRiskJointInspectionStore((s) => s.stocks);
  const accept = useRiskJointInspectionStore((s) => s.accept);
  const error = useRiskJointInspectionStore((s) => s.error);
  const [lines, setLines] = useState<MaterialLineInput[]>([]);
  const [passed, setPassed] = useState<boolean>(true);
  const [cost, setCost] = useState<string>("");

  const addLine = () => {
    const first = stocks[0];
    if (!first) return;
    setLines([...lines, { materialCode: first.materialCode, warehouse: first.warehouse, quantity: 1 }]);
  };
  const updateLine = (idx: number, patch: Partial<MaterialLineInput>) => {
    setLines(lines.map((line, i) => (i === idx ? { ...line, ...patch } : line)));
  };
  const pickStock = (idx: number, key: string) => {
    const stock = stocks.find((s) => `${s.warehouse}|${s.materialCode}` === key);
    if (stock) updateLine(idx, { materialCode: stock.materialCode, warehouse: stock.warehouse });
  };

  const submit = async () => {
    const ok = await accept(order.id, {
      materials: lines.filter((l) => l.quantity > 0),
      acceptancePassed: passed,
      costAmount: cost === "" ? undefined : Number(cost)
    });
    if (ok) onClose();
  };

  return (
    <div className="modal-mask" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <h2>领料并验收 · 维修单 #{order.id}</h2>
        <p className="muted">领料与验收在同一事务提交：验收不通过将整体回滚，不扣料也不关单。</p>

        <h3>领料明细</h3>
        {lines.map((line, idx) => (
          <div className="material-line" key={idx}>
            <select value={`${line.warehouse}|${line.materialCode}`} onChange={(e) => pickStock(idx, e.target.value)}>
              {stocks.map((s) => (
                <option key={`${s.warehouse}|${s.materialCode}`} value={`${s.warehouse}|${s.materialCode}`}>
                  {s.warehouse} · {s.materialName}（余 {s.quantity} {s.unit ?? ""}）
                </option>
              ))}
            </select>
            <input type="number" min={1} value={line.quantity}
              onChange={(e) => updateLine(idx, { quantity: Number(e.target.value) })} />
            <button onClick={() => setLines(lines.filter((_, i) => i !== idx))}>移除</button>
          </div>
        ))}
        <button onClick={addLine} disabled={stocks.length === 0}>+ 添加领料</button>

        <h3>验收结果</h3>
        <div className="row-actions">
          <label><input type="radio" checked={passed} onChange={() => setPassed(true)} /> 通过</label>
          <label><input type="radio" checked={!passed} onChange={() => setPassed(false)} /> 不通过</label>
        </div>
        <label>费用（元）
          <input type="number" min={0} value={cost} onChange={(e) => setCost(e.target.value)} placeholder="验收通过时记录" />
        </label>

        {error && <p className="error-text">{error.message}（{error.code}）</p>}
        <div className="form-actions">
          <button className="primary" onClick={submit}>提交领料并验收</button>
          <button onClick={onClose}>取消</button>
        </div>
      </div>
    </div>
  );
}

function OrdersPanel({ onAccept }: { onAccept: (order: RepairOrder) => void }) {
  const orders = useRiskJointInspectionStore((s) => s.orders);
  return (
    <div className="panel wide">
      <h2>维修单</h2>
      {orders.length === 0 ? <EmptyState title="暂无维修单" /> : (
        <table className="data-table">
          <thead>
            <tr><th>单号</th><th>级别</th><th>维修队</th><th>工作日期</th><th>状态</th><th>费用</th><th>操作</th></tr>
          </thead>
          <tbody>
            {orders.map((order) => (
              <tr key={order.id}>
                <td>#{order.id}</td>
                <td>{order.leak_level ? <StatusBadge value={order.leak_level} /> : "-"}</td>
                <td>{order.crew_name ?? `#${order.crew_id}`}</td>
                <td className="muted">{order.work_date ?? "-"}</td>
                <td><StatusBadge value={order.status} /></td>
                <td className="muted">{order.cost_amount == null ? "-" : `¥${order.cost_amount}`}</td>
                <td>
                  {order.status !== "CLOSED"
                    ? <button className="primary" onClick={() => onAccept(order)}>领料并验收</button>
                    : <span className="muted">已关闭 {order.finished_at ? formatDate(order.finished_at) : ""}</span>}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

function StockPanel() {
  const stocks = useRiskJointInspectionStore((s) => s.stocks);
  const usages = useRiskJointInspectionStore((s) => s.usages);
  const logs = useRiskJointInspectionStore((s) => s.logs);
  return (
    <>
      <div className="panel">
        <h2>材料库存</h2>
        {stocks.length === 0 ? <EmptyState title="暂无库存" /> : (
          <table className="data-table">
            <thead><tr><th>仓库</th><th>材料</th><th>库存</th></tr></thead>
            <tbody>
              {stocks.map((s) => (
                <tr key={`${s.warehouse}|${s.materialCode}`}>
                  <td>{s.warehouse}</td>
                  <td>{s.materialName}<div className="muted small">{s.materialCode}</div></td>
                  <td>{s.quantity} {s.unit ?? ""}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
      <div className="panel">
        <h2>材料流水</h2>
        {usages.length === 0 ? <EmptyState title="暂无领料流水" /> : (
          <div className="table">
            {usages.slice(0, 8).map((u) => (
              <article className="row" key={u.id}>
                <strong>单 #{u.repair_order_id} · {u.material_name}</strong>
                <span>{u.quantity} {u.unit}</span>
                <StatusBadge value={u.usage_status} />
              </article>
            ))}
          </div>
        )}
      </div>
      <div className="panel">
        <h2>操作日志</h2>
        {logs.length === 0 ? <EmptyState title="暂无操作日志" /> : (
          <div className="table">
            {logs.slice(0, 10).map((log) => (
              <article className="row" key={log.id}>
                <strong>{log.detail ?? log.action}</strong>
                <span className="muted">{log.createdAt ? formatDate(log.createdAt) : ""}</span>
              </article>
            ))}
          </div>
        )}
      </div>
    </>
  );
}

export function RiskJointInspectionPage() {
  const store = useRiskJointInspectionStore();
  const [accepting, setAccepting] = useState<RepairOrder | null>(null);

  useEffect(() => { store.loadAll(); }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const stats = useMemo(() => ({
    pending: store.queue.filter((r) => r.verify_status === "PENDING").length,
    dispatchable: store.queue.filter((r) => r.dispatchable).length,
    openOrders: store.orders.filter((o) => o.status !== "CLOSED").length,
    closedOrders: store.orders.filter((o) => o.status === "CLOSED").length
  }), [store.queue, store.orders]);

  return (
    <main className="page">
      <section className="page-head">
        <div>
          <p className="eyebrow">water-leak</p>
          <h1>风险联巡 · 漏损核查到维修验收</h1>
        </div>
        <StatusBadge value={store.backendOnline ? "ONLINE" : "OFFLINE_MOCK"} />
      </section>

      <section className="metrics">
        <StatCard label="待核实报告" value={stats.pending} />
        <StatCard label="可派工报告" value={stats.dispatchable} />
        <StatCard label="未关闭维修单" value={stats.openOrders} />
        <StatCard label="已关闭维修单" value={stats.closedOrders} />
      </section>

      {store.error && (
        <p className="error-text banner" onClick={store.clearMessages}>
          {store.error.message}（{store.error.code}）
        </p>
      )}
      {store.notice && (
        <p className="notice-text banner" onClick={store.clearMessages}>{store.notice}</p>
      )}

      <section className="workbench">
        <div className="col">
          <QueuePanel />
          <OrdersPanel onAccept={setAccepting} />
        </div>
        <div className="col">
          <ReportPanel />
          <StockPanel />
        </div>
      </section>

      {accepting && <AcceptModal order={accepting} onClose={() => setAccepting(null)} />}
    </main>
  );
}
