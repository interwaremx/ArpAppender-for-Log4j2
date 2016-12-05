![alt text][logo]
[logo]: http://reedlatam.com/sadmoweb/img/modulos/Listasweb/expo-tecnologia/2016/lista-expositores//logo_iw_soluciones_honestas.png "Interware de México"
-----
# [Arp Appender](http://www.interware.com.mx)   

Arp appender is a log4j 2.X appender to filter and send formated log messages to arp platform.

## Features
* Log4j appender
* Reconnection on error and disconnection

## Language
 * Pure java

## Requirements
 * Log4j 2.X

## Installation
1. Make sure you already set up log4j 1.X in your project
2. Add ArpAppender jar to your project's classpath
3. Add the ArpAppender configuration to your log4j configuration

## Configuration:
```
<?xml version="1.0" encoding="UTF-8"?>
<Configuration packages="mx.com.interware.arp.appender">
    <Appenders>
        <Console name="STDOUT" target="SYSTEM_OUT">
            <PatternLayout pattern="%d %-5p [%t] %C{2} (%F:%L) - %m%n"/>
        </Console>
        <ArquimedesAppender name="ArquimedesAppender"
                            host="localhost"
                            port="55555"
                            reconnectionTime="10000"
                            maxQueue="50"
                            sendDelta="1000">
            <regexp>
                <![CDATA[ 
                .*(EJECUTANDO) +([a-zA-Z0-9]+).*time .>>.*|.*(FINALIZANDO) +([a-zA-Z0-9]+).*time .>> +([0-9]+).*
                ]]>
            </regexp>
            <ednFormat>
            <![CDATA[ 
                :thread "%thread%", :timestamp %timestamp%, :level "%level%", :start "%s", :tx "%s", :end "%s", :tx "%s", :delta "%s"
            ]]>
            </ednFormat>
        </ArquimedesAppender>
    </Appenders>
    <Loggers>
        <Logger name="org.apache.log4j.xml" level="info"/>
        <Root level="debug">
            <AppenderRef ref="ArquimedesAppender" />
        </Root>
    </Loggers>
</Configuration>

```

## Features
For ednFormat key at properties file, there are some keywords:  
**%thread%** - Thread name  
**%timestamp%** - Timestamp of logging  
**%level%** - Logging level  
**%mensaje%** - Logging Message  
**%s** - Strings associated to regexp groups  


## Javadoc
https://interwaremx.github.io/ArpAppender/javadoc/

## Using Log4J 1.x?
https://github.com/interwaremx/ArpAppender-for-Log4j
