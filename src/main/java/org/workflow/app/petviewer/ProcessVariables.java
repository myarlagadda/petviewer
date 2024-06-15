package org.workflow.app.petviewer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@JsonInclude(Include.NON_NULL)
public class ProcessVariables {

  private String businessKey;
  private String petType;

  public String getPetType() {
    return petType;
  }

  public void setPetType(String petType) {
    this.petType = petType;
  }

  private Boolean result;

  public String getBusinessKey() {
    return businessKey;
  }

  public ProcessVariables setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
    return this;
  }

  public Boolean getResult() {
    return result;
  }

  public ProcessVariables setResult(Boolean result) {
    this.result = result;
    return this;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(
        this,
        new MultilineRecursiveToStringStyle() {
          public ToStringStyle withShortPrefixes() {
            this.setUseShortClassName(true);
            this.setUseIdentityHashCode(false);
            return this;
          }
        }.withShortPrefixes());
  }
}
