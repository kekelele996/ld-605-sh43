/** 风险联巡流程请求体类型：上报 / 核实 / 派工 / 领料验收。 */
export interface ReportInput {
  pointId: number;
  leakLevel: string;
  reporterType: string;
  description?: string;
  photoUrl?: string;
}

export interface DispatchInput {
  leakReportId: number;
  crewId: number;
  workDate?: string;
  priority?: string;
}

export interface MaterialLineInput {
  materialCode: string;
  warehouse: string;
  quantity: number;
}

export interface AcceptInput {
  materials: MaterialLineInput[];
  acceptancePassed: boolean;
  costAmount?: number;
  note?: string;
}
