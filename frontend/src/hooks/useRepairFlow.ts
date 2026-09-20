import { useState } from "react";
import type { AcceptPayload, DispatchPayload } from "../api/RepairOrder";
import { useRepairOrderStore } from "../stores/RepairOrderStore";

/** 维修闭环动作：派工 + 领料验收，统一 busy 状态，返回错误消息或 null。 */
export function useRepairFlow() {
  const dispatchAction = useRepairOrderStore((s) => s.dispatch);
  const acceptAction = useRepairOrderStore((s) => s.accept);
  const [busy, setBusy] = useState(false);

  async function dispatch(payload: DispatchPayload): Promise<string | null> {
    setBusy(true);
    try {
      return await dispatchAction(payload);
    } finally {
      setBusy(false);
    }
  }

  async function accept(id: number, payload: AcceptPayload): Promise<string | null> {
    setBusy(true);
    try {
      return await acceptAction(id, payload);
    } finally {
      setBusy(false);
    }
  }

  return { dispatch, accept, busy };
}
