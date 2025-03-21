package com.example.setu_spliwise.models;

import com.example.setu_spliwise.dtos.enums.ExpenseType;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Expense extends BaseModel {

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private double amount;

  @Column(nullable = false)
  private ExpenseType type;

  @ManyToOne
  @JoinColumns({
    @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
  })
  private UserGroup userGroup;

  @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
  private Set<Split> splits = new HashSet<>();
}
