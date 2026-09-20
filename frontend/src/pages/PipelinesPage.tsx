import { useEffect, useMemo, useState } from "react";
import { EmptyState } from "../components/common/EmptyState";
import { RiskBadge } from "../components/common/RiskBadge";
import { RiskLevel } from "../constants/RiskLevel";
import { useInspectionPointStore } from "../stores/InspectionPointStore";
import { usePipelineSegmentStore } from "../stores/PipelineSegmentStore";
import { formatRisk } from "../utils/formatters";

export function PipelinesPage() {
  const { rows: segments, loading, load } = usePipelineSegmentStore();
  const { rows: points, load: loadPoints } = useInspectionPointStore();
  const [risk, setRisk] = useState<string>("ALL");

  useEffect(() => {
    load();
    loadPoints();
  }, [load, loadPoints]);

  const filtered = useMemo(
    () => (risk === "ALL" ? segments : segments.filter((s) => s.risk_level === risk)),
    [segments, risk]
  );

  return (
    <main className="page">
      <section className="page-head">
        <div>
          <p className="eyebrow">water-leak</p>
          <h1>管网资产</h1>
        </div>
        <label className="filter">
          风险筛选
          <select value={risk} onChange={(e) => setRisk(e.target.value)}>
            <option value="ALL">全部</option>
            {RiskLevel.map((level) => <option key={level} value={level}>{formatRisk(level)}（{level}）</option>)}
          </select>
        </label>
      </section>

      <section className="panel wide">
        {filtered.length === 0 && !loading ? <EmptyState title="暂无管段" /> : (
          <table className="data-table">
            <thead>
              <tr><th>编码</th><th>片区</th><th>材质</th><th>管径</th><th>敷设年份</th><th>压力区</th><th>风险等级</th><th>巡检点数</th></tr>
            </thead>
            <tbody>
              {filtered.map((s) => (
                <tr key={s.id}>
                  <td>{s.segment_code}</td>
                  <td>{s.district}</td>
                  <td>{s.material}</td>
                  <td>{s.diameter}</td>
                  <td>{s.install_year}</td>
                  <td>{s.pressure_zone}</td>
                  <td><RiskBadge value={s.risk_level} /></td>
                  <td>{points.filter((p) => p.pipeline_segment_id === s.id).length}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
    </main>
  );
}
