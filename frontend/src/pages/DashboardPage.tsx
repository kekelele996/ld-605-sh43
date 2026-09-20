import { useEffect } from "react";
import { EmptyState } from "../components/common/EmptyState";
import { RiskBadge } from "../components/common/RiskBadge";
import { StatCard } from "../components/common/StatCard";
import { StatusBadge } from "../components/common/StatusBadge";
import { useLeakSeverity } from "../hooks/useLeakSeverity";
import { useInspectionPointStore } from "../stores/InspectionPointStore";
import { useLeakReportStore } from "../stores/LeakReportStore";
import { useMaterialUsageStore } from "../stores/MaterialUsageStore";
import { usePipelineSegmentStore } from "../stores/PipelineSegmentStore";
import { useRepairOrderStore } from "../stores/RepairOrderStore";
import { formatLeakLevel, formatRepairStatus } from "../utils/formatters";

export function DashboardPage() {
  const { rows: leaks, load: loadLeaks } = useLeakReportStore();
  const { rows: orders, load: loadOrders } = useRepairOrderStore();
  const { rows: points, load: loadPoints } = useInspectionPointStore();
  const { rows: segments, load: loadSegments } = usePipelineSegmentStore();
  const { stock, loadStock } = useMaterialUsageStore();
  const severity = useLeakSeverity(leaks);

  useEffect(() => {
    loadLeaks();
    loadOrders();
    loadPoints();
    loadSegments();
    loadStock();
  }, [loadLeaks, loadOrders, loadPoints, loadSegments, loadStock]);

  const openOrders = orders.filter((o) => o.status !== "CLOSED");
  const overduePoints = points.filter((p) => p.overdue_days > 0);
  const topLeaks = leaks.slice(0, 5);

  return (
    <main className="page">
      <section className="page-head">
        <div>
          <p className="eyebrow">water-leak</p>
          <h1>漏损态势</h1>
        </div>
      </section>

      <section className="metrics">
        <StatCard label="漏损报告" value={severity.total} />
        <StatCard label="待核实" value={severity.pendingVerify} />
        <StatCard label="未关闭维修单" value={openOrders.length} />
        <StatCard label="巡检超期点" value={overduePoints.length} />
        <StatCard label="库存物料" value={stock.length} />
      </section>

      <section className="workbench">
        <div className="panel wide">
          <h2>漏损核查优先级（BURST→TRACE / 风险高→低 / 超期优先）</h2>
          {topLeaks.length === 0 ? <EmptyState title="暂无漏损报告" /> : (
            <table className="data-table">
              <thead><tr><th>ID</th><th>等级</th><th>管段</th><th>风险</th><th>巡检超期</th><th>阻塞原因</th></tr></thead>
              <tbody>
                {topLeaks.map((row) => (
                  <tr key={row.id}>
                    <td>{row.id}</td>
                    <td><StatusBadge value={row.leak_level} text={formatLeakLevel(row.leak_level)} /></td>
                    <td>{row.segment_code}</td>
                    <td><RiskBadge value={row.risk_level} /></td>
                    <td className={row.overdue_days > 0 ? "overdue" : "ok-text"}>{row.overdue_days > 0 ? `超期 ${row.overdue_days} 天` : "未超期"}</td>
                    <td>{row.block_reason ? <span className="block-reason">{row.block_reason}</span> : "—"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
        <div className="panel">
          <h2>维修闭环</h2>
          {openOrders.length === 0 ? <EmptyState title="暂无未关闭维修单" /> : (
            <ul className="plain-list">
              {openOrders.map((o) => (
                <li key={o.id}>
                  <strong>#{o.id}</strong> {o.crew_name}
                  <StatusBadge value={o.status} text={formatRepairStatus(o.status)} />
                </li>
              ))}
            </ul>
          )}
        </div>
      </section>
    </main>
  );
}
