import { formatRisk } from "../../utils/formatters";

/** 管段风险等级徽标：LOW/MEDIUM/HIGH/EXTREME。 */
export function RiskBadge({ value }: { value: string }) {
  return (
    <span className={"badge risk-" + String(value).toLowerCase()}>
      {formatRisk(value)}
    </span>
  );
}
