package com.datio.akkatraining.config

import org.slf4j.{Logger, LoggerFactory}


/**
  * Logging
  * The default application logger, that can be injected where necessary.
  */
trait Logging {

  def log: Logger = LoggerFactory.getLogger(this.getClass.getName.replace("$", ""))

}
