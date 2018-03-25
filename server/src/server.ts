import app from "./app";
import { Request, Response, NextFunction } from "express";

app.set("port", Number.parseInt(process.env.PORT) || 8080);

const server = app.listen(app.get("port"), () => {
  console.log(
    "  App is running at http://localhost:%d in %s mode",
    app.get("port"),
    app.get("env")
  );
  console.log("  Press CTRL-C to stop\n");
});

export default server;
