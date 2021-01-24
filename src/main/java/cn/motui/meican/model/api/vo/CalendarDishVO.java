package cn.motui.meican.model.api.vo;

import cn.motui.meican.model.api.Dish;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author it.motui
 * @date 2021-01-17
 */
public class CalendarDishVO {
  private String name;
  private Integer priceInCent;
  private String priceString;
  private Integer originalPriceInCent;
  @JsonProperty("isSection")
  private Boolean section;
  private String actionRequiredLevel;
  private String actionRequiredReason;
  private Long id;

  public Dish toDish() {
    Dish dish = new Dish();
    dish.setSection(section);
    dish.setId(id);
    dish.setName(name);
    dish.setPriceString(priceString);
    dish.setOriginalPriceInCent(originalPriceInCent);
    return dish;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPriceInCent() {
    return priceInCent;
  }

  public void setPriceInCent(Integer priceInCent) {
    this.priceInCent = priceInCent;
  }

  public String getPriceString() {
    return priceString;
  }

  public void setPriceString(String priceString) {
    this.priceString = priceString;
  }

  public Integer getOriginalPriceInCent() {
    return originalPriceInCent;
  }

  public void setOriginalPriceInCent(Integer originalPriceInCent) {
    this.originalPriceInCent = originalPriceInCent;
  }

  public Boolean getSection() {
    return section;
  }

  public void setSection(Boolean section) {
    this.section = section;
  }

  public String getActionRequiredLevel() {
    return actionRequiredLevel;
  }

  public void setActionRequiredLevel(String actionRequiredLevel) {
    this.actionRequiredLevel = actionRequiredLevel;
  }

  public String getActionRequiredReason() {
    return actionRequiredReason;
  }

  public void setActionRequiredReason(String actionRequiredReason) {
    this.actionRequiredReason = actionRequiredReason;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
