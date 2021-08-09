package org.example.vertxtutorial.vertical;

import com.ibm.mq.MQQueue;
import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueueSession;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.example.vertxtutorial.utils.DateUtils;

import javax.jms.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.UUID;

public class NotiVerticle extends AbstractVerticle {
    private static final String SERVER_NOTI="192.168.57.18";
    private static final int PORT=7914;
    private static final String URL_API="/VietbankAPIs/v1/Noti/UnRegister";
    private static final String HOST = "localhost";
    private static final int PORT_QUEUE = 1414;
    private static final String CHANNEL = "MYCHANNEL";
    private static final String QMGR = "TESTQM";
    private static final String APP_USER = "ace";
    private static final String APP_PASSWORD = "123456";
    private static final String QUEUE_NAME = "TEST.TUANNV.IN";
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        WebClient client = WebClient.create(vertx);
        MessageConsumer<Object> consumer =  vertx.eventBus().consumer("vietbank.noti.unregister");
        consumer.handler(message -> {
        //System.out.println("received a message: " + message.body());
        System.out.println("[Worker] Consuming data in " + Thread.currentThread().getName());
           try{
            client
                    .post(PORT, SERVER_NOTI, URL_API)
                    .sendJsonObject(createRequest(message.body()))
                    .onSuccess(res -> {
                        // OK
                        JsonObject jsonObject = res.body().toJsonObject();
                        message.reply(jsonObject);
                    })
                    .onFailure(err ->{
                        message.fail(500,err.getMessage());
                        System.out.println("fail");
                    });
        }catch (Exception e){
            message.fail(500,e.getMessage());
        }
        });

//        this.vertx.eventBus().consumer("vietbank.noti.unregister", (message) -> {
//            System.out.println("I have received a message: " + message.body());
//            JmsConnectionFactory connectionFactory = createConnectionToQueue();
//            setJMSProperties(connectionFactory);
//            try {
//                //JMSContext context = connectionFactory.createContext();
//                //Destination destination = context.createQueue("queue:///"+QUEUE_NAME);
//                //JMSProducer producer= context.createProducer();
//                //producer.send(destination,message.body().toString());
//                //sendATextMessage(connectionFactory, destination, message.body().toString());
//                System.out.println("--Sent text message.");
//            } catch (Exception var7) {
//                if (var7 instanceof JMSException) {
//                    JMSException jmse = (JMSException)var7;
//                 if (jmse.getLinkedException() != null) {
//                     System.out.println("!! JMS exception thrown in application main method !!");
//                      System.out.println(jmse.getLinkedException());
//                   } else {
//                       jmse.printStackTrace();
//                  }
//                } else {
//                    System.out.println("!! Failure in application main method !!");
//                    var7.printStackTrace();
//                }
//            }
//            message.reply(message.body());
//        });
    }
    private static JmsConnectionFactory createConnectionToQueue(){
        JmsConnectionFactory cf;
        try {
            JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
            cf = ff.createConnectionFactory();
        } catch (JMSException var3) {
            System.out.println("JMS Exception when trying to create connection factory!");
            if (var3.getLinkedException() != null) {
                System.out.println(var3.getLinkedException());
            } else {
                var3.printStackTrace();
            }

            cf = null;
        }

        return cf;
    }
    private static void setJMSProperties(JmsConnectionFactory cf) {
        try {
            cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, HOST);
            cf.setIntProperty(WMQConstants.WMQ_PORT, PORT_QUEUE);
            cf.setStringProperty(WMQConstants.WMQ_CHANNEL, CHANNEL);
            cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
            cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, QMGR);
            cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutGet (JMS)");
            //cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP,true);
            //cf.setStringProperty(WMQConstants.USERID, APP_USER);
            //cf.setStringProperty(WMQConstants.PASSWORD, APP_PASSWORD);
        } catch (JMSException var2) {
            System.out.println("JMS Exception when trying to set JMS properties!");
            if (var2.getLinkedException() != null) {
                System.out.println(var2.getLinkedException());
            } else {
                var2.printStackTrace();
            }
        }

    }

    public static void sendATextMessage(JmsConnectionFactory connectionFactory, Destination destination, String payload) {
        try {
            new BufferedReader(new InputStreamReader(System.in));
            System.out.print("payload : ");
            JMSContext producerContext = connectionFactory.createContext();
            JMSProducer producer = producerContext.createProducer();
            Message m = producerContext.createTextMessage(payload);
            producer.send(destination, m);
            producerContext.close();
        } catch (Exception var7) {
            System.out.println("Exception when trying to send a text message!");
            if (var7 instanceof JMSException) {
                JMSException jmse = (JMSException)var7;
                if (jmse.getLinkedException() != null) {
                    System.out.println(jmse.getLinkedException());
                } else {
                    jmse.printStackTrace();
                }
            } else {
                var7.printStackTrace();
            }
        }

    }
    private JsonObject createRequest(Object data){
        JsonObject trace =new JsonObject();
        trace.put("requestID", UUID.randomUUID().toString());
        trace.put("requestDateTime", DateUtils.date2Str(new Date(), "yyyy-MM-dd") + "T" + DateUtils.date2Str(new Date(), "HH:mm:ss.SSS") + "+07:00");
        trace.put("channelID","Dev");
        trace.put("clientID","DevService");
        JsonObject dataRequest = new JsonObject();
        dataRequest.put("trace",trace);
        dataRequest.put("signature","");
        dataRequest.put("data",data);
        return dataRequest;
    }
}
