import { useEffect } from "react";
import { ChecklistPanel } from "../components/common/ChecklistPanel";
import { EmptyState } from "../components/common/EmptyState";
import { RiskBadge } from "../components/common/RiskBadge";
import { StatusBadge } from "../components/common/StatusBadge";
import { useInspectionPointStore } from "../stores/InspectionPointStore";
import { formatDate, formatOverdue } from "../utils/formatters";

export function InspectionsPage() {
  const { rows, loading, load } = useInspectionPointStore();

  useEffect(() => {
    load();
  }, [load]);

  const overdue = rows.filter((p) => p.overdue_days > 0);

  return (
    <main className="page">
      <section className="page-head">
        <div>
          <p className="eyebrow">water-leak</p>
          <h1>巡检任务</h1>
        </div>
        <StatusBadge value={overdue.length > 0 ? "OVERDUE" : "ON_TRACK"} text={overdue.length > 0 ? `${overdue.length} 点超期` : "全部按期"} />
      </section>

      <section className="workbench">
        <div className="panel wide">
          <h2>巡检点周期任务</h2>
          {rows.length === 0 && !loading ? <EmptyState title="暂无巡检点" /> : (
            <table className="data-table">
              <thead>
                <tr><th>点位</th><th>类型</th><th>所属管段</th><th>风险</th><th>周期</th><th>最近巡检</th><th>超期</th><th>状态</th></tr>
              </thead>
              <tbody>
                {rows.map((p) => (
                  <tr key={p.id}>
                    <td>{p.point_code}</td>
                    <td>{p.point_type}</td>
                    <td>{p.segment_code}</td>
                    <td><RiskBadge value={p.risk_level} /></td>
                    <td>{p.check_frequency}</td>
                    <td>{p.last_checked_at ? formatDate(p.last_checked_at) : "从未巡检"}</td>
                    <td className={p.overdue_days > 0 ? "overdue" : "ok-text"}>{formatOverdue(p.overdue_days)}</td>
                    <td><StatusBadge value={p.status} /></td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
        <div className="panel">
          <ChecklistPanel
            title="现场巡检要点"
            items={[
              { label: "井室积水与渗水痕迹", done: true },
              { label: "阀门 / 消火栓启闭状态", done: true },
              { label: "听漏仪异常音排查", done: false },
              { label: "拍照上传并提交记录", done: false }
            ]}
          />
          <p className="hint">发现异常请前往「漏损核查」页面上报，核查排序会参考本页超期情况。</p>
        </div>
      </section>
    </main>
  );
}
