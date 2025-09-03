import tech.wetech.flexmodel.codegen.AbstractGenerator
import tech.wetech.flexmodel.codegen.GenerationContext
import tech.wetech.flexmodel.codegen.ModelClass

import java.nio.file.Path

class ServiceGenerator extends AbstractGenerator {

  @Override
  String getTargetFile(GenerationContext context, String targetDirectory) {
    ModelClass modelClass = context.modelClass
    return Path.of('src', 'services', modelClass.shortClassName.toLowerCase() + '.service.ts').toString()
  }

  @Override
  void writeModel(PrintWriter out, GenerationContext context) {
    def modelClass = context.modelClass
    out.println "import { Injectable } from '@nestjs/common'"
    out.println "import { InjectRepository } from '@nestjs/typeorm'"
    out.println "import { Repository, In } from 'typeorm'"
    out.println "import { ${modelClass.shortClassName} } from '../entities/${modelClass.shortClassName}'"
    out.println ""
    out.println "@Injectable()"
    out.println "export class ${modelClass.shortClassName}Service {"
    out.println "  constructor(@InjectRepository(${modelClass.shortClassName}) private readonly repo: Repository<${modelClass.shortClassName}>) {}"
    out.println ""
    out.println "  list(): Promise<${modelClass.shortClassName}[]> {"
    out.println "    return this.repo.find({ order: { id: 'ASC' } })"
    out.println "  }"
    out.println ""
    out.println "  async page(offset: number, limit: number): Promise<${modelClass.shortClassName}[]> {"
    out.println "    return this.repo.find({ skip: offset, take: limit, order: { id: 'ASC' } })"
    out.println "  }"
    out.println ""
    out.println "  get(id: number): Promise<${modelClass.shortClassName}> {"
    out.println "    return this.repo.findOneByOrFail({ id } as any)"
    out.println "  }"
    out.println ""
    out.println "  async create(entity: ${modelClass.shortClassName}): Promise<number> {"
    out.println "    const saved = await this.repo.save(entity)"
    out.println "    return (saved as any).id"
    out.println "  }"
    out.println ""
    out.println "  async createBatch(entities: ${modelClass.shortClassName}[]): Promise<number> {"
    out.println "    const saved = await this.repo.save(entities)"
    out.println "    return saved.length"
    out.println "  }"
    out.println ""
    out.println "  async update(entity: ${modelClass.shortClassName}): Promise<void> {"
    out.println "    await this.repo.save(entity)"
    out.println "  }"
    out.println ""
    out.println "  async delete(id: number): Promise<void> {"
    out.println "    await this.repo.delete(id)"
    out.println "  }"
    out.println ""
    out.println "  async deleteByIds(ids: number[]): Promise<void> {"
    out.println "    await this.repo.delete(ids)"
    out.println "  }"
    out.println ""
    out.println "  findByIds(ids: number[]): Promise<${modelClass.shortClassName}[]> {"
    out.println "    return this.repo.find({ where: { id: In(ids) } as any, order: { id: 'ASC' } })"
    out.println "  }"
    out.println ""
    out.println "  count(): Promise<number> {"
    out.println "    return this.repo.count()"
    out.println "  }"
    out.println ""
    out.println "  async existsById(id: number): Promise<boolean> {"
    out.println "    const c = await this.repo.count({ where: { id } as any })"
    out.println "    return c > 0"
    out.println "  }"
    out.println "}"
  }
}
