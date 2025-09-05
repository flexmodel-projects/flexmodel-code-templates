package tech.wetech.flexmodel.codegen;

import java.util.Map;

/**
 * Template information including name and default variables
 * 
 * @author cjbi
 */
public record TemplateInfo(String name, Map<String, Object> variables) {
  
  /**
   * Get template name
   */
  public String getName() {
    return name;
  }
  
  /**
   * Get default variables for this template
   */
  public Map<String, Object> getVariables() {
    return variables;
  }
}