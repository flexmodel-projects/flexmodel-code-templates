import tech.wetech.flexmodel.codegen.AbstractGenerator
import tech.wetech.flexmodel.codegen.GenerationContext
import tech.wetech.flexmodel.codegen.ModelClass

import java.nio.file.Path

class NodeControllerGenerator extends AbstractGenerator {

  @Override
  String getTargetFile(GenerationContext context, String targetDirectory) {
    ModelClass modelClass = context.modelClass
    return Path.of(targetDirectory, modelClass.shortClassName.toLowerCase() + '.controller.ts').toString()
  }

  @Override
  void writeModel(PrintWriter out, GenerationContext context) {
    def modelClass = context.modelClass
    def basePath = '/' + modelClass.shortClassName.toLowerCase() + 's'
    out.println "import { Controller, Get, Post, Put, Delete, Param, Body, Query, HttpCode, HttpStatus } from '@nestjs/common'"
    out.println "import { ${modelClass.shortClassName}Service } from '../services/${modelClass.shortClassName.toLowerCase()}.service'"
    out.println "import { ${modelClass.shortClassName} } from '../entities/${modelClass.shortClassName}'"
    out.println ""
    out.println "@Controller('${basePath.substring(1)}')"
    out.println "export class ${modelClass.shortClassName}Controller {"
    out.println "  constructor(private readonly service: ${modelClass.shortClassName}Service) {}"
    out.println ""
    out.println "  @Get()"
    out.println "  list(): Promise<${modelClass.shortClassName}[]> {"
    out.println "    return this.service.list()"
    out.println "  }"
    out.println ""
    out.println "  @Get('page')"
    out.println "  page(@Query('offset') offset: number, @Query('limit') limit: number): Promise<${modelClass.shortClassName}[]> {"
    out.println "    return this.service.page(Number(offset), Number(limit))"
    out.println "  }"
    out.println ""
    out.println "  @Get(':id')"
    out.println "  get(@Param('id') id: number): Promise<${modelClass.shortClassName}> {"
    out.println "    return this.service.get(Number(id))"
    out.println "  }"
    out.println ""
    out.println "  @Post()"
    out.println "  @HttpCode(HttpStatus.CREATED)"
    out.println "  create(@Body() entity: ${modelClass.shortClassName}): Promise<number> {"
    out.println "    return this.service.create(entity)"
    out.println "  }"
    out.println ""
    out.println "  @Post('batch')"
    out.println "  @HttpCode(HttpStatus.CREATED)"
    out.println "  createBatch(@Body() entities: ${modelClass.shortClassName}[]): Promise<number> {"
    out.println "    return this.service.createBatch(entities)"
    out.println "  }"
    out.println ""
    out.println "  @Put(':id')"
    out.println "  update(@Param('id') id: number, @Body() entity: ${modelClass.shortClassName}): Promise<void> {"
    out.println "    entity.id = Number(id)"
    out.println "    return this.service.update(entity)"
    out.println "  }"
    out.println ""
    out.println "  @Delete(':id')"
    out.println "  @HttpCode(HttpStatus.NO_CONTENT)"
    out.println "  delete(@Param('id') id: number): Promise<void> {"
    out.println "    return this.service.delete(Number(id))"
    out.println "  }"
    out.println ""
    out.println "  @Delete()"
    out.println "  @HttpCode(HttpStatus.NO_CONTENT)"
    out.println "  deleteByIds(@Query('ids') ids: number[]): Promise<void> {"
    out.println "    const list = Array.isArray(ids) ? ids.map(Number) : String(ids).split(',').map(v => Number(v))"
    out.println "    return this.service.deleteByIds(list)"
    out.println "  }"
    out.println ""
    out.println "  @Get('ids')"
    out.println "  findByIds(@Query('ids') ids: number[]): Promise<${modelClass.shortClassName}[]> {"
    out.println "    const list = Array.isArray(ids) ? ids.map(Number) : String(ids).split(',').map(v => Number(v))"
    out.println "    return this.service.findByIds(list)"
    out.println "  }"
    out.println ""
    out.println "  @Get('count')"
    out.println "  count(): Promise<number> {"
    out.println "    return this.service.count()"
    out.println "  }"
    out.println ""
    out.println "  @Get('exists/:id')"
    out.println "  existsById(@Param('id') id: number): Promise<boolean> {"
    out.println "    return this.service.existsById(Number(id))"
    out.println "  }"
    out.println "}"
  }
}
