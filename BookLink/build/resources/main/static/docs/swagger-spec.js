window.swaggerSpec={
  "openapi" : "3.0.1",
  "info" : {
    "title" : "Interview-Project",
    "description" : "Interview-Project SwaggerUI.",
    "version" : "0.0.1"
  },
  "servers" : [ {
    "url" : "http://localhost:8080"
  } ],
  "tags" : [ ],
  "paths" : {
    "/api/member/double-check/email" : {
      "post" : {
        "tags" : [ "api" ],
        "operationId" : "member_controller_test/email_double_check/",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/api-member-double-check-email-1076505861"
              },
              "examples" : {
                "member_controller_test/email_double_check/" : {
                  "value" : "{\r\n  \"email\" : \"test@naver.com\"\r\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/api-member-double-check-email486549215"
                },
                "examples" : {
                  "member_controller_test/email_double_check/" : {
                    "value" : "{\r\n  \"status\" : \"OK\",\r\n  \"message\" : \"중복되지않는 이메일\",\r\n  \"data\" : null\r\n}"
                  }
                }
              }
            }
          }
        }
      }
    }
  },
  "components" : {
    "schemas" : {
      "api-member-double-check-email-1076505861" : {
        "type" : "object",
        "properties" : {
          "email" : {
            "type" : "string",
            "description" : "test@naver.com"
          }
        }
      },
      "api-member-double-check-email486549215" : {
        "type" : "object"
      }
    }
  }
}