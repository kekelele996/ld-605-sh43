import { useState, type ComponentType } from "react";
import { createRoot } from "react-dom/client";
import { DashboardPage } from "./pages/DashboardPage";
import { InspectionsPage } from "./pages/InspectionsPage";
import { LeaksPage } from "./pages/LeaksPage";
import { PipelinesPage } from "./pages/PipelinesPage";
import { RepairsPage } from "./pages/RepairsPage";
import { routes } from "./router/routes";
import "./styles.css";

const pages: Record<string, ComponentType> = {
  "/dashboard": DashboardPage,
  "/pipelines": PipelinesPage,
  "/inspections": InspectionsPage,
  "/leaks": LeaksPage,
  "/repairs": RepairsPage
};

function App() {
  const [active, setActive] = useState<string>(routes[0]?.route ?? "/dashboard");
  const Current = pages[active] ?? DashboardPage;
  return (
    <div className="shell">
      <aside>
        <div className="brand">城市水务漏损巡检平台</div>
        <nav>
          {routes.map((route) => (
            <button key={route.route} className={active === route.route ? "active" : ""} onClick={() => setActive(route.route)}>
              {route.name}
            </button>
          ))}
        </nav>
      </aside>
      <Current />
    </div>
  );
}

createRoot(document.getElementById("root")!).render(<App />);
