//package ch.uzh.ifi.hase.soprafs23.websocket;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class WebSocketControllerTest {
//
//    @Test
//    void configureMessageBrokerTest() {
//        // Create an instance of WebSocketConfig
//        WebSocketConfig webSocketConfig = new WebSocketConfig();
//
//        // Create a mock MessageBrokerRegistry
//        MessageBrokerRegistry registry;
//        registry = new MessageBrokerRegistry();
//
//        // Call the configureMessageBroker method
//        webSocketConfig.configureMessageBroker(registry);
//
//        // Verify the configuration
//        assertEquals("/topic", registry.getSimpleBrokerRegistry().getPrefix());
//        assertEquals("/app", registry.getApplicationDestinationPrefixes().iterator().next());
//    }
//}
