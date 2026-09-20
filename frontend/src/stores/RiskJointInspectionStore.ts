import { create } from "zustand";
import { toFailure, type ApiFailure } from "../api/client";
import { fetchVerificationQueue, reportLeak, verifyLeak } from "../api/LeakReport";
import { acceptOrder, dispatchOrder, listRepairOrder } from "../api/RepairOrder";
import { listInspectionPoint } from "../api/InspectionPoint";
import { listRepairCrew } from "../api/RepairCrew";
import { listMaterialStock } from "../api/MaterialStock";
import { listMaterialUsage } from "../api/MaterialUsage";
import { listAuditLog } from "../api/AuditLog";
import type { VerificationQueueItem } from "../types/VerificationQueueItem";
import type { RepairOrder } from "../types/RepairOrder";
import type { InspectionPoint } from "../types/InspectionPoint";
import type { RepairCrew } from "../types/RepairCrew";
import type { MaterialStock } from "../types/MaterialStock";
import type { MaterialUsage } from "../types/MaterialUsage";
import type { AuditLog } from "../types/AuditLog";
import type { AcceptInput, DispatchInput, ReportInput } from "../types/RepairFlow";

type State = {
  queue: VerificationQueueItem[];
  orders: RepairOrder[];
  points: InspectionPoint[];
  crews: RepairCrew[];
  stocks: MaterialStock[];
  usages: MaterialUsage[];
  logs: AuditLog[];
  loading: boolean;
  backendOnline: boolean;
  error: ApiFailure | null;
  notice: string | null;
  loadAll: () => Promise<void>;
  report: (input: ReportInput) => Promise<boolean>;
  verify: (id: number, status: string) => Promise<boolean>;
  dispatch: (input: DispatchInput) => Promise<boolean>;
  accept: (id: number, input: AcceptInput) => Promise<boolean>;
  clearMessages: () => void;
};

export const useRiskJointInspectionStore = create<State>((set, get) => {
  /** 动作统一入口：成功后整库重取（刷新后状态一致），失败时保留阻塞原因。 */
  async function run(action: () => Promise<unknown>, okNotice: string): Promise<boolean> {
    set({ error: null, notice: null });
    try {
      await action();
      await get().loadAll();
      set({ notice: okNotice });
      return true;
    } catch (err) {
      set({ error: toFailure(err) });
      await get().loadAll().catch(() => undefined);
      return false;
    }
  }

  return {
    queue: [],
    orders: [],
    points: [],
    crews: [],
    stocks: [],
    usages: [],
    logs: [],
    loading: false,
    backendOnline: true,
    error: null,
    notice: null,

    async loadAll() {
      set({ loading: true });
      try {
        const [queue, orders, points, crews, stocks, usages, logs] = await Promise.all([
          fetchVerificationQueue(),
          listRepairOrder(),
          listInspectionPoint(),
          listRepairCrew(),
          listMaterialStock(),
          listMaterialUsage(),
          listAuditLog()
        ]);
        set({ queue, orders, points, crews, stocks, usages, logs, loading: false, backendOnline: true });
      } catch {
        set({ loading: false, backendOnline: false });
      }
    },

    report: (input) => run(() => reportLeak(input), "漏损已上报，等待核实"),
    verify: (id, status) => run(() => verifyLeak(id, status), status === "VERIFIED" ? "已核实通过" : "已驳回"),
    dispatch: (input) => run(() => dispatchOrder(input), "派工成功"),
    accept: (id, input) => run(() => acceptOrder(id, input), "验收通过，维修单已关闭"),
    clearMessages: () => set({ error: null, notice: null })
  };
});
