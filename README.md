# 城市水务漏损巡检平台

面向水务公司的管网资产、漏损上报、巡检任务和维修闭环管理系统。本版本打通「风险联巡 → 维修验收」闭环：漏损核查按 BURST→TRACE、管段风险从高到低排序（同分看巡检超期），BURST 未核实不能派工，同一维修队同一天只能有一张未关闭维修单（并发派工只成功一单），领料和验收写在同一事务（验收失败不得扣料或关单），页面刷新后仍能读到状态与阻塞原因。

## 快速启动

```bash
cp .env.example .env && docker compose up -d
```

## 访问地址或 CLI 示例

前端：<http://localhost:20105>

后端健康检查：<http://localhost:21105/health>

核心业务接口：

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/api/leak-report` | 漏损核查列表（BURST→TRACE → 管段风险高→低 → 巡检超期优先） |
| POST | `/api/leak-report` | 漏损上报（居民/巡检员） |
| POST | `/api/leak-report/{id}/verify` | 漏损核实（VERIFIED / REJECTED） |
| GET | `/api/repair-order` | 维修单列表（含维修队、阻塞原因） |
| GET | `/api/repair-order/crews` | 维修队列表 |
| POST | `/api/repair-order/dispatch` | 派工（BURST 未核实拒绝；同队同日一单） |
| POST | `/api/repair-order/{id}/accept` | 领料 + 验收（同一事务，失败整体回滚） |
| GET | `/api/material-usage?repairOrderId=` | 材料流水 |
| GET | `/api/material-usage/stock` | 仓库库存 |

## 业务规则

1. **核查排序**：漏损等级 BURST→MAJOR→MINOR→TRACE；同级按管段风险 EXTREME→HIGH→MEDIUM→LOW；再同分按巡检超期天数降序（`last_checked_at + check_frequency` 与当前时间比较）。
2. **派工约束**：`leak_level=BURST` 且 `verify_status≠VERIFIED` 时拒绝派工（409 `BURST_UNVERIFIED`）；同一维修队同一 `dispatch_date` 只能存在一张 `status≠CLOSED` 的维修单——服务端悲观锁维修队行串行化，数据库部分唯一索引 `uniq_repair_crew_day_open` 兜底，并发派工只成功一单（409 `CREW_DAY_CONFLICT`）。
3. **领料验收同事务**：`POST /api/repair-order/{id}/accept` 在一个事务内完成库存扣减（悲观锁库存行）+ 材料流水 + 关单；`acceptancePassed=false` 或库存不足时整体回滚，不扣料、不关单（409 `ACCEPTANCE_FAILED` / `STOCK_INSUFFICIENT`）。
4. **阻塞原因持久化**：派工被阻塞写入 `leak_report.block_reason`，验收失败写入 `repair_order.block_reason`（独立事务提交，业务回滚不影响），列表接口实时返回，页面刷新后仍能读到状态与阻塞原因；BURST 未核实的阻塞原因同时支持实时推导。
5. **操作日志**：所有写操作落 `audit_log` 表；失败路径用独立事务保留失败痕迹。

## 本地开发方式

- 前端：`cd frontend && npm install && npm run dev`（端口 20105，`/api` 代理到 21105）
- 后端（无 Docker/无 PostgreSQL 时，内存 H2 + 种子数据）：
  `cd backend && mvn spring-boot:run -Dspring-boot.run.profiles=local`（端口 21105）
- 后端（连接本地 PostgreSQL）：`DB_HOST=localhost DB_PORT=5432 DB_NAME=app_db DB_USER=app_user DB_PASSWORD=app_password mvn spring-boot:run`

## 技术栈

| 层 | 技术 |
|---|---|
| 前端 | React 18 + TypeScript + Vite + Ant Design + ECharts + Zustand |
| 后端 | Spring Boot 3 + Java 17 + JPA |
| 数据库 | PostgreSQL 15（本地开发可用 H2 内存库） |
| 部署 | Docker Compose |

## 项目目录结构

```text
frontend/src/
├── api/            # 按实体分文件 + http.ts 请求封装（解析 {code,message} 错误）
├── stores/         # Zustand 按实体分文件，动作返回错误消息
├── types/          # 实体与视图类型（LeakReportView / RepairOrderView / ...）
├── constants/      # 枚举、错误码、日志模板、状态文案
├── constructors/   # 按实体拆默认对象和表单构造器
├── components/common/  # StatusBadge / RiskBadge / MaterialTable / TimelineList / ChecklistPanel ...
├── hooks/          # useLeakSeverity / useRepairFlow / usePagination
├── pages/          # Dashboard / Pipelines / Inspections / Leaks / Repairs
├── router/  utils/  mocks/
backend/src/main/java/com/generated/waterLeak/
├── controllers/    # 按实体分文件，记录日志后重新抛出业务异常
├── services/       # 业务规则与事务边界（dispatch / accept / verify）
├── models/         # JPA 实体（含 repair_crew / material_stock / audit_log）
├── repositories/   # Spring Data JPA，含悲观锁与联查视图查询
├── middlewares/    # ErrorHandlerMiddleware 等
├── constants/      # 枚举、错误码、日志模板
├── constructors/   # 响应 DTO 工厂
├── types/          # 请求 Payload / 视图记录 / BizException
├── routes/  config/  utils/
backend/src/main/resources/
├── application.yml          # 默认 PostgreSQL（环境变量注入）
├── application-local.yml    # 本地 H2 内存库
└── data.sql                 # H2 种子数据（仅嵌入式数据源执行）
database/init.sql            # PostgreSQL 建表 + 唯一索引 + 种子数据
```

## 环境变量说明

- `COMPOSE_PROJECT_NAME`：Compose 项目名，默认 `water-leak`
- `FRONTEND_PORT`：前端端口，默认 `20105`
- `BACKEND_PORT`：后端端口，默认 `21105`
- `DB_PORT`：数据库宿主机端口，默认 `54320`
- `DB_USER` / `DB_PASSWORD` / `DB_NAME`：数据库凭据
- `JWT_SECRET`：JWT 签名密钥

## Docker 部署说明

- 根 Compose 文件不写 `version`，顶层 `name: water-leak`。
- 容器名均使用 `${COMPOSE_PROJECT_NAME:-water-leak}` 前缀，任意目录名（含中文）可启动。
- 数据库使用命名卷 `db_data`，不绑定宿主机路径。
- `db` 配置 `pg_isready` healthcheck，后端 `depends_on: service_healthy` 等待；后端 `/health` 健康检查通过后前端才启动。
- 常见问题：端口占用时修改 `.env` 中端口后重启；需要重置数据时执行 `docker compose down -v`。

## 枚举/常量出现位置清单

- **LeakLevel**（TRACE/MINOR/MAJOR/BURST）：前端 `constants/LeakLevel.ts`、`types/LeakLevel.ts`、`utils/formatters.ts`（formatLeakLevel）、`pages/LeaksPage.tsx`（筛选与展示）、`components/common/StatusBadge.tsx`；后端 `constants/LeakLevel.java`（rank 排序）、`services/LeakReportService.java`、`services/RepairOrderService.java`（BURST 派工拦截）、`constants/ErrorMessages.java`、`constants/LogTemplates.java`。
- **RepairStatus**（WAIT_ASSIGN/ASSIGNED/WORKING/ACCEPTANCE/CLOSED）：前端 `constants/RepairStatus.ts`、`types/RepairStatus.ts`、`utils/formatters.ts`（formatRepairStatus）、`pages/RepairsPage.tsx`；后端 `constants/RepairStatus.java`、`services/RepairOrderService.java`（关单与未关闭判定）、`repositories/RepairOrderRepository.java`。
- **RiskLevel**（LOW/MEDIUM/HIGH/EXTREME）：前端 `constants/RiskLevel.ts`、`types/RiskLevel.ts`、`components/common/RiskBadge.tsx`、`pages/PipelinesPage.tsx`（风险筛选）；后端 `constants/RiskLevel.java`（rank 排序）、`services/LeakReportService.java`。
- **VerifyStatus**（PENDING/VERIFIED/REJECTED）：前端 `constants/VerifyStatus.ts`、`pages/LeaksPage.tsx`；后端 `constants/VerifyStatus.java`、`services/LeakReportService.java`、`services/RepairOrderService.java`。
- **UsageStatus**（ISSUED/CONSUMED/RETURNED）：前端 `constants/UsageStatus.ts`、`components/common/MaterialTable.tsx`；后端 `constants/UsageStatus.java`、`services/RepairOrderService.java`。
- **错误码**：前端 `constants/errorCodes.ts` + `constants/errorMessages.ts`；后端 `constants/ErrorCodes.java` + `constants/ErrorMessages.java` + `middlewares/ErrorHandlerMiddleware.java`。

## 为什么会牵一发动全身

实体字段、枚举、日志模板、错误消息、构造器、筛选器和展示组件被刻意拆散到多个目录：漏损等级同时出现在前端常量/类型/格式化/页面与后端枚举/排序服务/错误消息/日志模板中；新增一个枚举值或字段，需要同步类型、构造器、service、controller、store、页面、README 与 `database/init.sql`、`backend/src/main/resources/data.sql` 两份种子。

## License

MIT
