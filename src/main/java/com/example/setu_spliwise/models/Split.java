package com.example.setu_spliwise.models;

import javax.persistence.*;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Split extends BaseModel {

  @Column(nullable = false)
  private double amount;

  @Column(nullable = false)
  private double percentage;

  @Column(nullable = false)
  private boolean isPaid = false;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "expense_id", nullable = false)
  private Expense expense;
}
