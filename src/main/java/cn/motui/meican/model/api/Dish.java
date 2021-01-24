package cn.motui.meican.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * 菜品
 *
 * @author it.motui
 * @date 2021-01-17
 */
public class Dish {
  private Long id;
  private Long dishSectionId;
  private String name;
  private Integer priceInCent;
  private String priceString;
  private Integer originalPriceInCent;
  @JsonProperty("isSection")
  private Boolean section;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getDishSectionId() {
    return dishSectionId;
  }

  public void setDishSectionId(Long dishSectionId) {
    this.dishSectionId = dishSectionId;
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

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Dish)) {
      return false;
    }
    Dish dish = (Dish) o;
    return Objects.equal(id, dish.id) && Objects.equal(dishSectionId, dish.dishSectionId) && Objects.equal(name, dish.name) && Objects.equal(priceInCent, dish.priceInCent) && Objects.equal(priceString, dish.priceString) && Objects.equal(originalPriceInCent, dish.originalPriceInCent) && Objects.equal(section, dish.section);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, dishSectionId, name, priceInCent, priceString, originalPriceInCent, section);
  }

  public String getTitle() {
    return this.name.substring(0, this.name.indexOf("("));
  }

  public String getRemark() {
    return this.name.substring(this.name.indexOf("(") + 1, this.name.indexOf(")"));
  }
}
