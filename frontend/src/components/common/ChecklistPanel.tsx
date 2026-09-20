export interface ChecklistItem {
  label: string;
  done: boolean;
}

/** 验收要点清单：领料验收弹窗内展示。 */
export function ChecklistPanel({ title, items }: { title: string; items: ChecklistItem[] }) {
  return (
    <div className="checklist">
      <strong>{title}</strong>
      <ul>
        {items.map((item, index) => (
          <li key={index} className={item.done ? "done" : ""}>
            {item.done ? "✓" : "○"} {item.label}
          </li>
        ))}
      </ul>
    </div>
  );
}
