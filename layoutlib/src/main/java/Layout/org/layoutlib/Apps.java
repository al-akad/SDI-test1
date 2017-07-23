package Layout.org.layoutlib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Apps {

    String Can_Adr;

    public  void SendDatajson(){


        try {

            JSONObject jsonObject1 = new JSONObject();
            JSONObject jsonObject2 = new JSONObject();
            JSONObject jsonObject3 = new JSONObject();
            JSONObject jsonObject4 = new JSONObject();
            JSONObject jsonObject5 = new JSONObject();
            JSONObject jsonObject6 = new JSONObject();
            JSONObject jsonObject7 = new JSONObject();
            JSONObject jsonObject8 = new JSONObject();
            JSONObject jsonObject9 = new JSONObject();
            JSONObject jsonObject10 = new JSONObject();
            JSONObject jsonObject11 = new JSONObject();
            JSONObject jsonObject12 = new JSONObject();
            JSONObject jsonObject13 = new JSONObject();
            JSONObject jsonObject14 = new JSONObject();
            JSONObject jsonObject15 = new JSONObject();
            JSONObject jsonObject16 = new JSONObject();
            JSONObject jsonObject17 = new JSONObject();
            JSONObject jsonObject18 = new JSONObject();
            JSONObject jsonObject19 = new JSONObject();
            JSONObject jsonObject20 = new JSONObject();
            JSONObject jsonObject21 = new JSONObject();
            JSONObject jsonObject22 = new JSONObject();
            JSONObject jsonObject23 = new JSONObject();
            JSONObject jsonObject24 = new JSONObject();
            JSONObject jsonObject25 = new JSONObject();
            JSONObject jsonObject26 = new JSONObject();
            JSONObject jsonObject27 = new JSONObject();
            JSONObject jsonObject28 = new JSONObject();
            JSONObject jsonObject29 = new JSONObject();
            JSONObject jsonObject30 = new JSONObject();
            JSONObject jsonObject31 = new JSONObject();
            JSONObject jsonObject32 = new JSONObject();
            JSONObject jsonObject33 = new JSONObject();


            JSONArray jsonArray1 = new JSONArray();
            JSONArray jsonArray2 = new JSONArray();



            jsonObject1.put("name", "Can_Adr");
            jsonObject1.put("unit", "4");
            jsonObject1.put("value", "394");
            jsonObject1.put("type","int");



            jsonObject2.put("name", "Can_Length");
            jsonObject2.put("unit", "4");
            jsonObject2.put("value", "7");
            jsonObject2.put("type","int");


            jsonObject3.put("name", "Can_Offset");
            jsonObject3.put("unit", "4");
            jsonObject3.put("value", "0");
            jsonObject3.put("type","int");


            jsonObject4.put("name", "Can_Write_Adr");
            jsonObject4.put("unit", "4");
            jsonObject4.put("value", "394");
            jsonObject4.put("type","int");


            jsonObject5.put("name", "Can_Write_Length");
            jsonObject5.put("unit", "4");
            jsonObject5.put("value", "7");
            jsonObject5.put("type","int");


            jsonObject6.put("name", "Can_Write_Offset");
            jsonObject6.put("unit", "4");
            jsonObject6.put("value", "0");
            jsonObject6.put("type","int");

            jsonObject7.put("name", "Can_Write_Value");
            jsonObject7.put("unit", "4");
            jsonObject7.put("value", "7");
            jsonObject7.put("type","int");



            jsonObject8.put("Can_Adr", jsonObject1);
            jsonObject8.put("Can_Length", jsonObject2);
            jsonObject8.put("Can_Offset", jsonObject3);
            jsonObject8.put("Can_Write_Adr", jsonObject4);
            jsonObject8.put("Can_Write_Length", jsonObject5);
            jsonObject8.put("Can_Write_Offset", jsonObject6);
            jsonObject8.put("Can_Write_Value", jsonObject7);






            jsonObject16.put("name", "Mapper_Offset");
            jsonObject16.put("unit", "4");
            jsonObject16.put("value", "0");
            jsonObject16.put("type","int");


            jsonObject17.put("name", "Mapper_Size");
            jsonObject17.put("unit", "4");
            jsonObject17.put("value", "30");
            jsonObject17.put("type","int");



            jsonObject18.put("name", "Mapper_Type");
            jsonObject18.put("unit", "4");
            jsonObject18.put("value", "0");
            jsonObject18.put("type","int");




            jsonObject19.put("Mapper_Offset", jsonObject16);
            jsonObject20.put("Mapper_Size", jsonObject17);
            jsonObject21.put("Mapper_Type", jsonObject18);

            jsonObject22.put("Mapper_Offset",jsonObject16);
            jsonObject22.put("Mapper_Size",jsonObject17);
            jsonObject22.put("Mapper_Type",jsonObject18);




            jsonObject23.put("src_port_nr", "0");
            jsonObject23.put("dest_port_nr", "0");

            jsonObject24.put("src_port_nr", "1");
            jsonObject24.put("dest_port_nr","1");

            jsonArray1.put(jsonObject23);
            jsonArray1.put(jsonObject24);







            jsonObject25.put("name", "MAPPER_APP");
            jsonObject25.put("type","MAPPER_APP");
            jsonObject25.put("params", jsonObject22);








            jsonObject28.put("name", "DEBUG_APP");
            jsonObject28.put("type", "DEBUG_APP");
            jsonObject28.put("params",jsonObject27);





            jsonObject29.put("src","CAN_APP_DECODER");
            jsonObject29.put("dest","DEBUG_APP");
            jsonObject29.put("ports",jsonArray1);



            jsonObject30.put("src","DEBUG_APP");
            jsonObject30.put("dest","MAPPER_APP");
            jsonObject30.put("ports",jsonArray1);


            jsonObject31.put("name","CAN_APP_DECODER");
            jsonObject31.put("type","CAN_APP_DECODER");
            jsonObject31.put("params",jsonObject8);







            jsonObject32.put("CAN_APP_DECODER",jsonObject31);
            jsonObject32.put("DEBUG_APP",jsonObject28);
            jsonObject32.put("MAPPER_APP",jsonObject25);

            jsonArray2.put(jsonObject29);
            jsonArray2.put(jsonObject30);


            jsonObject33.put("apps", jsonObject32);
            jsonObject33.put("connections",jsonArray2);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
//    {
//        "apps": {
//               "CAN_APP_DECODER": {
//                "name": "CAN_APP_DECODER",
//                "type": "CAN_APP_DECODER",
//                "params": {
//                           "Can_Adr": {
//                           "name": "Can_Adr",
//                            "unit": 4,
//                            "value": 394,
//                            "type": "int"
//                             },
//                            "Can_Length": {
//                            "name": "Can_Length",
//                            "unit": 4,
//                            "value": 7,
//                            "type": "int"
//                             },
//                            "Can_Offset": {
//                            "name": "Can_Offset",
//                            "unit": 4,
//                            "value": 0,
//                            "type": "int"
//                             },
//                            "Can_Write_Adr": {
//                            "name": "Can_Write_Adr",
//                            "unit": 4,
//                           "value": 394,
//                            "type": "int"
//                             },
//                            "Can_Write_Length": {
//                            "name": "Can_Write_Length",
//                            "unit": 4,
//                            "value": 7,
//                            "type": "int"
//                             },
//                            "Can_Write_Offset": {
//                            "name": "Can_Write_Offset",
//                            "unit": 4,
//                            "value": 0,
//                            "type": "int"
//                             },
//                            "Can_Write_Value": {
//                            "name": "Can_Write_Value",
//                            "unit": 4,
//                            "value": 7,
//                            "type": "int"
//                             }
//                     }
//                },
//                "DEBUG_APP": {
//                             "name": "DEBUG_APP",
//                             "type": "DEBUG_APP",
//                             "params": {}
//                },
//               "MAPPER_APP": {
//                             "name": "MAPPER_APP",
//                             "type": "MAPPER_APP",
//                             "params": {
//                                       "Mapper_Offset": {
//                                                         "name": "Mapper_Offset",
//                                                         "unit": 4,
//                                                         "value": 0,
//                                                         "type": "int"
//                                                        },
//                                       "Mapper_Size": {
//                                                      "name": "Mapper_Size",
//                                                      "unit": 4,
//                                                      "value": 30,
//                                                      "type": "int"
//                                                      },
//                                      "Mapper_Type": {
//                                                      "name": "Mapper_Type",
//                                                      "unit": 4,
//                                                      "value": 0,
//                                                      "type": "int"
//                                                     }
//                                      }
//                             }
//               },
//        "connections": [
//                          {"src": "CAN_APP_DECODER",
//                           "dest": "DEBUG_APP",
//                           "ports": [
//                                      {"src_port_nr": 0,
//                                       "dest_port_nr": 0
//                                      },
//                                      {"src_port_nr": 1,
//                                       "dest_port_nr": 1
//                                       }
//                                    ]
//                          },
//                          {"src": "DEBUG_APP",
//                           "dest": "MAPPER_APP",
//                           "ports": [
//                                      {"src_port_nr": 0,
//                                       "dest_port_nr": 0
//                                      },
//                                      {"src_port_nr": 1,
//                                       "dest_port_nr": 1
//                                      }
//                                    ]
//                           }
//                       ]
//    }

        }

