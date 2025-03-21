package com.example.setu_spliwise.exceptions;

public class BadRequestException extends RuntimeException {
  public BadRequestException(String msg) {
    super(msg);
  }
}
