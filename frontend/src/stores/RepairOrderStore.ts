import { create } from "zustand";
import {
  acceptRepairOrder,
  dispatchRepairOrder,
  listRepairCrew,
  listRepairOrder,
  type AcceptPayload,
  type DispatchPayload
} from "../api/RepairOrder";
import { ApiError } from "../api/http";
import type { RepairCrew } from "../types/RepairCrew";
import type { RepairOrderView } from "../types/RepairOrderView";

type State = {
  rows: RepairOrderView[];
  crews: RepairCrew[];
  loading: boolean;
  load: () => Promise<void>;
  loadCrews: () => Promise<void>;
  /** 派工：成功返回 null，失败返回错误消息（BURST 未核实 / 维修队当日冲突）。 */
  dispatch: (payload: DispatchPayload) => Promise<string | null>;
  /** 领料 + 验收：成功返回 null，失败返回错误消息。 */
  accept: (id: number, payload: AcceptPayload) => Promise<string | null>;
};

function toMessage(e: unknown): string {
  if (e instanceof ApiError) return e.message;
  if (e instanceof Error) return e.message;
  return "操作失败，请稍后重试";
}

export const useRepairOrderStore = create<State>((set, get) => ({
  rows: [],
  crews: [],
  loading: false,
  async load() {
    set({ loading: true });
    set({ rows: await listRepairOrder(), loading: false });
  },
  async loadCrews() {
    set({ crews: await listRepairCrew() });
  },
  async dispatch(payload) {
    try {
      await dispatchRepairOrder(payload);
      await get().load();
      return null;
    } catch (e) {
      await get().load().catch(() => undefined);
      return toMessage(e);
    }
  },
  async accept(id, payload) {
    try {
      await acceptRepairOrder(id, payload);
      await get().load();
      return null;
    } catch (e) {
      await get().load().catch(() => undefined);
      return toMessage(e);
    }
  }
}));
