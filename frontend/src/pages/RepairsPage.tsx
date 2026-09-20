import { Fragment, useEffect, useState } from "react";
import { listMaterialUsage } from "../api/MaterialUsage";
import type { AcceptItemPayload } from "../api/RepairOrder";
import { ChecklistPanel } from "../components/common/ChecklistPanel";
import { EmptyState } from "../components/common/EmptyState";
import { MaterialTable } from "../components/common/MaterialTable";
import { StatCard } from "../components/common/StatCard";
import { StatusBadge } from "../components/common/StatusBadge";
import { TimelineList, type TimelineItem } from "../components/common/TimelineList";
import { useRepairFlow } from "../hooks/useRepairFlow";
import { useMaterialUsageStore } from "../stores/MaterialUsageStore";
import { useRepairOrderStore } from "../stores/RepairOrderStore";
import type { MaterialUsage } from "../types/MaterialUsage";
import type { RepairOrderView } from "../types/RepairOrderView";
import { formatDate, formatLeakLevel, formatMoney, formatRepairStatus } from "../utils/formatters";

type Notice = { kind: "ok" | "err"; text: string } | null;

export function RepairsPage() {
  const { rows, loading, load } = useRepairOrderStore();
  const { stock, loadStock } = useMaterialUsageStore();

  const [notice, setNotice] = useState<Notice>(null);
  const [acceptTarget, setAcceptTarget] = useState<RepairOrderView | null>(null);
  const [expanded, setExpanded] = useState<number | null>(null);
  const [materials, setMaterials] = useState<MaterialUsage[]>([]);

  useEffect(() => {
    load();
    loadStock();
  }, [load, loadStock]);

  const open = rows.filter((o) => o.status !== "CLOSED");
  const today = new Date().toISOString().slice(0, 10);
  const todayCount = rows.filter((o) => o.dispatch_date === today).length;
  const blocked = open.filter((o) => o.block_reason).length;
  const totalCost = rows.reduce((sum, o) => sum + (o.cost_amount ?? 0), 0);

  async function toggleExpand(order: RepairOrderView) {
    if (expanded === order.id) {
      setExpanded(null);
      return;
    }
    setMaterials(await listMaterialUsage(order.id));
    setExpanded(order.id);
  }

  function timelineOf(order: RepairOrderView): TimelineItem[] {
    const items: TimelineItem[] = [];
    if (order.created_at) items.push({ time: formatDate(order.created_at), text: `派工 ${order.crew_name}（${order.dispatch_date ?? "—"}）`, tone: "info" });
    if (order.block_reason) items.push({ time: "阻塞", text: order.block_reason, tone: "warn" });
    if (order.finished_at) items.push({ time: formatDate(order.finished_at), text: `验收通过，维修单关闭，费用 ${formatMoney(order.cost_amount)}`, tone: "ok" });
    return items;
  }

  return (
    <main className="page">
      <section className="page-head">
        <div>
          <p className="eyebrow">water-leak</p>
          <h1>维修闭环</h1>
        </div>
      </section>

      <p className="hint">领料和验收写在同一事务：验收失败不得扣料或关单。派工请前往「漏损核查」。</p>

      {notice && <p className={"notice " + notice.kind}>{notice.text}</p>}

      <section className="metrics">
        <StatCard label="未关闭维修单" value={open.length} />
        <StatCard label="今日派工" value={todayCount} />
        <StatCard label="验收受阻" value={blocked} />
        <StatCard label="累计费用" value={formatMoney(totalCost)} />
      </section>

      <section className="panel wide">
        <h2>维修单</h2>
        {rows.length === 0 && !loading ? <EmptyState title="暂无维修单" /> : (
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th><th>漏损报告</th><th>等级</th><th>维修队</th><th>优先级</th><th>状态</th>
                <th>派工日期</th><th>完成时间</th><th>费用</th><th>阻塞原因</th><th>操作</th>
              </tr>
            </thead>
            <tbody>
              {rows.map((order) => (
                <Fragment key={order.id}>
                  <tr>
                    <td>{order.id}</td>
                    <td>#{order.leak_report_id}</td>
                    <td>{order.leak_level ? <StatusBadge value={order.leak_level} text={formatLeakLevel(order.leak_level)} /> : "—"}</td>
                    <td>{order.crew_name}</td>
                    <td>{order.priority}</td>
                    <td><StatusBadge value={order.status} text={formatRepairStatus(order.status)} /></td>
                    <td>{order.dispatch_date ?? "—"}</td>
                    <td>{order.finished_at ? formatDate(order.finished_at) : "—"}</td>
                    <td>{formatMoney(order.cost_amount)}</td>
                    <td>{order.block_reason ? <span className="block-reason">{order.block_reason}</span> : "—"}</td>
                    <td className="actions">
                      {order.status !== "CLOSED" && (
                        <button className="btn ghost" onClick={() => setAcceptTarget(order)}>领料验收</button>
                      )}
                      <button className="btn ghost" onClick={() => toggleExpand(order)}>
                        {expanded === order.id ? "收起" : "材料/流转"}
                      </button>
                    </td>
                  </tr>
                  {expanded === order.id && (
                    <tr className="detail-row">
                      <td colSpan={11}>
                        <div className="detail-grid">
                          <div>
                            <h3>材料流水</h3>
                            <MaterialTable rows={materials} />
                          </div>
                          <div>
                            <h3>流转记录</h3>
                            <TimelineList items={timelineOf(order)} />
                          </div>
                        </div>
                      </td>
                    </tr>
                  )}
                </Fragment>
              ))}
            </tbody>
          </table>
        )}
      </section>

      <section className="panel">
        <h2>仓库库存</h2>
        {stock.length === 0 ? <EmptyState title="暂无库存" /> : (
          <table className="data-table">
            <thead><tr><th>物料</th><th>名称</th><th>仓库</th><th>现存</th></tr></thead>
            <tbody>
              {stock.map((s) => (
                <tr key={s.id}><td>{s.material_code}</td><td>{s.material_name}</td><td>{s.warehouse}</td><td>{s.quantity}</td></tr>
              ))}
            </tbody>
          </table>
        )}
      </section>

      {acceptTarget && (
        <AcceptModal
          order={acceptTarget}
          stock={stock}
          onClose={() => setAcceptTarget(null)}
          onDone={async (message) => {
            setAcceptTarget(null);
            await loadStock();
            setNotice(message ? { kind: "ok", text: message } : null);
          }}
          onError={(text) => setNotice({ kind: "err", text })}
        />
      )}
    </main>
  );
}

function AcceptModal({ order, stock, onClose, onDone, onError }: {
  order: RepairOrderView;
  stock: { id: number; material_code: string; material_name: string; warehouse: string; quantity: number }[];
  onClose: () => void;
  onDone: (message: string | null) => Promise<void>;
  onError: (text: string) => void;
}) {
  const { accept, busy } = useRepairFlow();
  const [items, setItems] = useState<AcceptItemPayload[]>([]);
  const [passed, setPassed] = useState(true);
  const [note, setNote] = useState("");
  const [cost, setCost] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [checklist, setChecklist] = useState([
    { label: "漏点止水完成", done: false },
    { label: "路面 / 绿化恢复", done: false },
    { label: "材料清点核销", done: false }
  ]);

  function addItem() {
    const first = stock[0];
    if (!first) return;
    setItems([...items, { materialCode: first.material_code, materialName: first.material_name, quantity: 1, unit: "个", warehouse: first.warehouse }]);
  }

  function pickStock(index: number, key: string) {
    const found = stock.find((s) => s.material_code + "@" + s.warehouse === key);
    if (!found) return;
    const next = [...items];
    next[index] = { ...next[index], materialCode: found.material_code, materialName: found.material_name, warehouse: found.warehouse };
    setItems(next);
  }

  async function submit() {
    const err = await accept(order.id, {
      acceptancePassed: passed,
      costAmount: cost === "" ? undefined : Number(cost),
      note: note || undefined,
      items
    });
    if (err) {
      setError(err);
      onError(err);
      return;
    }
    await onDone("验收通过：材料已扣减，维修单已关闭");
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-head"><h2>领料 + 验收：维修单 #{order.id}</h2><button className="btn ghost" onClick={onClose}>关闭</button></div>
        <p className="hint">领料与验收在同一事务提交：验收不通过或库存不足时整体回滚，不扣料、不关单。</p>

        <h3>领料明细</h3>
        {items.map((item, index) => (
          <div className="form-row material-row" key={index}>
            <select value={item.materialCode + "@" + item.warehouse} onChange={(e) => pickStock(index, e.target.value)}>
              {stock.map((s) => (
                <option key={s.id} value={s.material_code + "@" + s.warehouse}>
                  {s.material_code} {s.material_name}（{s.warehouse} 存 {s.quantity}）
                </option>
              ))}
            </select>
            <input type="number" min={0.5} step={0.5} value={item.quantity}
              onChange={(e) => setItems(items.map((it, i) => i === index ? { ...it, quantity: Number(e.target.value) } : it))} />
            <input className="unit" value={item.unit} placeholder="单位"
              onChange={(e) => setItems(items.map((it, i) => i === index ? { ...it, unit: e.target.value } : it))} />
            <button className="btn ghost" onClick={() => setItems(items.filter((_, i) => i !== index))}>删除</button>
          </div>
        ))}
        <button className="btn ghost" onClick={addItem} disabled={stock.length === 0}>+ 添加材料</button>

        <h3>验收结论</h3>
        <ChecklistPanel
          title="验收要点"
          items={checklist}
        />
        <div className="form-row checklist-toggle">
          {checklist.map((c, i) => (
            <label key={c.label}>
              <input type="checkbox" checked={c.done}
                onChange={(e) => setChecklist(checklist.map((x, j) => j === i ? { ...x, done: e.target.checked } : x))} />
              {c.label}
            </label>
          ))}
        </div>
        <label className="form-row"><span>验收结果</span>
          <select value={passed ? "pass" : "fail"} onChange={(e) => setPassed(e.target.value === "pass")}>
            <option value="pass">通过（关闭维修单）</option>
            <option value="fail">不通过（整体回滚）</option>
          </select>
        </label>
        <label className="form-row"><span>费用（元）</span>
          <input type="number" min={0} value={cost} onChange={(e) => setCost(e.target.value)} placeholder="验收通过时记录" />
        </label>
        <label className="form-row"><span>备注</span>
          <textarea rows={2} value={note} onChange={(e) => setNote(e.target.value)} placeholder="验收不通过时请说明原因" />
        </label>

        {error && <p className="notice err">{error}</p>}
        <div className="modal-actions">
          <button className="btn primary" disabled={busy} onClick={submit}>{busy ? "提交中…" : "提交领料与验收"}</button>
        </div>
      </div>
    </div>
  );
}
