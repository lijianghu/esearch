## DSL
```
{
  "pin_yin_knowledge_2" : {
    "aliases" : {
      "pin_yin" : { },
      "pin_yin_knowledge" : { }
    },
    "mappings" : {
      "properties" : {
        "id" : {
          "type" : "keyword"
        },
        "level" : {
          "type" : "integer"
        },
        "name" : {
          "type" : "keyword",
          "fields" : {
            "full_pin_yin_ik" : {
              "type" : "text",
              "analyzer" : "pinyinFull_ik"
            },
            "full_pin_yin_n_gram" : {
              "type" : "text",
              "analyzer" : "pinyinFull_ngram"
            },
            "simple_pin_yin_ik" : {
              "type" : "text",
              "analyzer" : "pinyiSimple_ik"
            },
            "simple_pin_yin_n_gram" : {
              "type" : "text",
              "analyzer" : "pinyiSimple_ngram"
            }
          }
        },
        "resType" : {
          "type" : "integer"
        },
        "stageId" : {
          "type" : "integer"
        },
        "subjectId" : {
          "type" : "integer"
        }
      }
    },
    "settings" : {
      "index" : {
        "refresh_interval" : "5s",
        "number_of_shards" : "1",
        "provided_name" : "pin_yin_knowledge_2",
        "max_result_window" : "10000000",
        "creation_date" : "1607571266589",
        "analysis" : {
          "filter" : {
            "pinyin_simple_filter" : {
              "keep_joined_full_pinyin" : "true",
              "lowercase" : "true",
              "none_chinese_pinyin_tokenize" : "false",
              "padding_char" : " ",
              "keep_original" : "true",
              "keep_first_letter" : "true",
              "keep_separate_first_letter" : "false",
              "type" : "pinyin",
              "keep_full_pinyin" : "false"
            },
            "pinyin_full_filter" : {
              "keep_joined_full_pinyin" : "true",
              "lowercase" : "true",
              "keep_none_chinese_in_joined_full_pinyin" : "true",
              "keep_original" : "false",
              "keep_none_chinese_together" : "true",
              "keep_first_letter" : "false",
              "keep_separate_first_letter" : "false",
              "type" : "pinyin",
              "keep_none_chinese" : "true",
              "limit_first_letter_length" : "50",
              "keep_full_pinyin" : "true"
            }
          },
          "analyzer" : {
            "pinyiSimple_ngram" : {
              "filter" : [
                "pinyin_simple_filter",
                "lowercase"
              ],
              "type" : "custom",
              "tokenizer" : "ngram_1_2_tokenizer"
            },
            "pinyinFull_ik" : {
              "filter" : [
                "asciifolding",
                "lowercase",
                "pinyin_full_filter"
              ],
              "type" : "custom",
              "tokenizer" : "ik_max_word"
            },
            "pinyinFull_ngram" : {
              "filter" : [
                "asciifolding",
                "lowercase",
                "pinyin_full_filter"
              ],
              "type" : "custom",
              "tokenizer" : "ngram_1_2_tokenizer"
            },
            "pinyiSimple_ik" : {
              "filter" : [
                "pinyin_simple_filter",
                "lowercase"
              ],
              "type" : "custom",
              "tokenizer" : "ik_max_word"
            }
          },
          "tokenizer" : {
            "ngram_1_2_tokenizer" : {
              "type" : "ngram",
              "min_gram" : "1",
              "max_gram" : "2"
            }
          }
        },
        "number_of_replicas" : "0",
        "uuid" : "6fuqqpapTcuv2_fQEdfocg",
        "version" : {
          "created" : "7010199"
        }
      }
    }
  }
}

```