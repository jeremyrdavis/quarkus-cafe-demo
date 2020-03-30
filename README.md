kafka-console-consumer --bootstrap-server localhost:9092 --topic orders --from-beginning
kafka-console-producer --broker-list localhost:9092 --topic orders

{"eventType":"BEVERAGE_ORDER_IN","item":"BLACK_COFFEE","itemId":"fd2af2b9-8d97-443d-bed8-371f2782a8b3","name":"Brady","orderId":"9103dd6b-ed58-423f-90b2-5cc4314996fg"}
{"eventType":"KITCHEN_ORDER_IN","item":"MUFFIN","itemId":"fd2af2b9-8d97-443d-bed8-371f2782a8b3","name":"Brady","orderId":"9103dd6b-ed58-423f-90b2-5cc4314996fg"}
