![alt text][logo]
[logo]: http://reedlatam.com/sadmoweb/img/modulos/Listasweb/expo-tecnologia/2016/lista-expositores//logo_iw_soluciones_honestas.png "Interware de México"
-----
# [Arp Appender](http://www.interware.com.mx)   

Arp appender is a log4j 1.X appender to filter and send formated log messages to arp platform.

## Features
* Log4j appender
* Reconnection on error and disconnection

## Language
 * Purely java

## Requirements
 * Log4j 1.X

## Installation
1. Make sure you already set up log4j 1.X in your project
2. Add ArpAppender jar to your project's classpath

## Properties:
log4j.appender.arquimides=mx.com.interware.arp.appender.ArquimidesAppender
log4j.appender.arquimides.regexp=.*(EJECUTANDO) +([a-zA-Z0-9]+).*time .>>.*|.*(FINALIZANDO) +([a-zA-Z0-9]+).*time .>> +([0-9]+).* 
log4j.appender.arquimides.ednFormat=:thread "%thread%", :timestamp %timestamp%, :level "%level%", :start "%s", :tx "%s", :end "%s", :tx "%s", :delta "%s"
log4j.appender.arquimides.host=127.0.0.1
log4j.appender.arquimides.port=55555
log4j.appender.arquimides.reconnectionTime=10000
log4j.appender.arquimides.maxQueue=5
log4j.appender.arquimides.sendDelta=1000

## Features
For ednFormat key at properties file, there are some keywords:
%thread% - Thread name
%timestamp% - Timestamp of logging
%level% - Logging level
%mensaje% - Logging Message
%s - Strings associated to regexp groups.

#Console
log4j.appender.console=org.apache.log4j.ConsoleAppender
log4j.appender.console.layout=org.apache.log4j.PatternLayout

# File
log4j.appender.FILE=org.apache.log4j.FileAppender
log4j.appender.FILE.File=${log}/log.out
log4j.appender.FILE.layout=org.apache.log4j.PatternLayout
log4j.appender.FILE.layout.conversionPattern=%m%n


#Javadoc
https://interwaremx.github.io/ArpAppender/javadoc/

