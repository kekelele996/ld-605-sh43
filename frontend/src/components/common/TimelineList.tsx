import { EmptyState } from "./EmptyState";

export interface TimelineItem {
  time: string;
  text: string;
  tone?: "ok" | "warn" | "info";
}

/** 时间线：漏损核查与维修闭环页共用，展示状态流转与阻塞原因。 */
export function TimelineList({ items }: { items: TimelineItem[] }) {
  if (items.length === 0) return <EmptyState title="暂无流转记录" />;
  return (
    <ul className="timeline">
      {items.map((item, index) => (
        <li key={index} className={"timeline-item " + (item.tone ?? "info")}>
          <span className="timeline-time">{item.time}</span>
          <span className="timeline-text">{item.text}</span>
        </li>
      ))}
    </ul>
  );
}
