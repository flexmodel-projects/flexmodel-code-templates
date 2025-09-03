# NestJS + TypeORM 模板

本模板参考 `java_springboot_mybatis_sample`，提供基于 NestJS + TypeORM 的代码生成骨架，用于从 FlexModel 模型生成基础的 CRUD 代码结构。

包含内容：
- 基础工程文件：`package.json`、`nest-cli.json`、`tsconfig*.json`
- 应用入口：`src/main.ts`、`src/app.module.ts`
- 代码生成器（Groovy）：`EntityGenerator.groovy`、`ControllerGenerator.groovy`、`ServiceGenerator.groovy`、`ModuleGenerator.groovy`

生成规则（简述）：
- 实体：将主键字段映射为 `@PrimaryGeneratedColumn()` `number`，其余字段映射为 `@Column()`，Java 类型到 TS 的简单映射：`Long/Integer/Short/Byte/Double/Float -> number`，`Boolean -> boolean`，其他 -> `string`。
- 服务：使用 `@nestjs/typeorm` 注入 `Repository`，实现 `list/page/get/create/createBatch/update/delete/deleteByIds/findByIds/count/existsById`。
- 控制器：REST 风格路由，基路径为实体名小写复数，如 `users`。
- 模块：注册实体仓库与服务、控制器。

使用说明（示例）：
1. 在 FlexModel 中选择本模板并生成到目标目录。
2. 进入生成目录：
   ```bash
   npm install
   npm run start:dev
   ```
3. 根据需要修改 TypeORM 连接配置（环境变量或 `src/app.module.ts`）。

注意：本模板默认使用 PostgreSQL（依赖 `pg`）。如需 MySQL/SQLite，请调整依赖与 `forRoot` 配置。


