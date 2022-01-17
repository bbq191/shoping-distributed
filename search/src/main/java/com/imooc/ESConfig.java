package com.imooc;

import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

/** @author afu */
@Configuration
public class ESConfig {

  /** 解决netty引起的issue */
  @PostConstruct
  void init() {
    System.setProperty("es.set.netty.runtime.available.processors", "false");
  }
}
