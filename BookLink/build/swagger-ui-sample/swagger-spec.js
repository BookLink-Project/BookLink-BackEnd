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
    "/api/member/email/double-check" : {
      "post" : {
        "tags" : [ "api" ],
        "operationId" : "member_controller_test/email_double_check/",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/api-member-email-double-check-1076505861"
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
            "description" : "200"
          }
        }
      }
    }
  },
  "components" : {
    "schemas" : {
      "api-member-email-double-check-1076505861" : {
        "type" : "object",
        "properties" : {
          "email" : {
            "type" : "string",
            "description" : "test@naver.com"
          }
        }
      }
    }
  }
}