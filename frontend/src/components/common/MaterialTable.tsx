import type { MaterialUsage } from "../../types/MaterialUsage";
import { UsageStatusText } from "../../constants/UsageStatus";
import { StatusBadge } from "./StatusBadge";
import { EmptyState } from "./EmptyState";

/** 材料流水表：维修闭环页与领料弹窗共用。 */
export function MaterialTable({ rows }: { rows: MaterialUsage[] }) {
  if (rows.length === 0) return <EmptyState title="暂无材料流水" />;
  return (
    <table className="data-table">
      <thead>
        <tr><th>物料编码</th><th>名称</th><th>数量</th><th>单位</th><th>仓库</th><th>状态</th></tr>
      </thead>
      <tbody>
        {rows.map((row) => (
          <tr key={row.id}>
            <td>{row.material_code}</td>
            <td>{row.material_name}</td>
            <td>{row.quantity}</td>
            <td>{row.unit}</td>
            <td>{row.warehouse}</td>
            <td><StatusBadge value={row.usage_status} text={UsageStatusText[row.usage_status as keyof typeof UsageStatusText] ?? row.usage_status} /></td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
