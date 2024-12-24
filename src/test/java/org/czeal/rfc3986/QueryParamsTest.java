/*
 * Copyright (C) 2024 Hideki Ikeda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.czeal.rfc3986;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;


class QueryParamsTest
{
    @Test
    void parse()
    {
        assertDoesNotThrow(() -> QueryParams.parse("k1=v1&k2=v2"));
        assertDoesNotThrow(() -> QueryParams.parse("k1=v1&k2"));
        assertDoesNotThrow(() -> QueryParams.parse("k1=v1&k2"));
        assertDoesNotThrow(() -> QueryParams.parse("k1=v1&"));
        assertDoesNotThrow(() -> QueryParams.parse(""));
        assertDoesNotThrow(() -> QueryParams.parse(null));
    }


    @Test
    void add()
    {
        assertDoesNotThrow(() -> QueryParams.parse("k1=v1&k2=v2").add("k3", "v3"));
        assertDoesNotThrow(() -> QueryParams.parse("k1=v1&k2").add("k3", null));
        assertDoesNotThrow(() -> QueryParams.parse("k1=v1&k2").add("k3", ""));
    }


    @Test
    void replace()
    {
        assertDoesNotThrow(() -> QueryParams.parse("k1=v1&k2=v2").replace("k2", "new-value"));
        assertDoesNotThrow(() -> QueryParams.parse("k1=v1&k2=v2").replace("k3", "v3"));
        assertDoesNotThrow(() -> QueryParams.parse("k1=v1&k2").replace("k2", "new-value"));
        assertDoesNotThrow(() -> QueryParams.parse("k1=v1&k2=v2").replace(null, "new-value"));
    }


    @Test
    void remove()
    {
        assertDoesNotThrow(() -> QueryParams.parse("k1=v1&k2=v2").remove("k2"));
        assertDoesNotThrow(() -> QueryParams.parse("k1=v1&k2=v2-1&k2=v2-2").remove("k2"));
        assertDoesNotThrow(() -> QueryParams.parse("k1=v1&k2=v2").remove(""));
        assertDoesNotThrow(() -> QueryParams.parse("k1=v1&k2=v2").remove(null));
    }


    @Test
    void to_string()
    {
        assertThat(QueryParams.parse("k1=v1&k2=v2").toString()).isEqualTo("k1=v1&k2=v2");
        assertThat(QueryParams.parse("k1=v1&k2").toString()).isEqualTo("k1=v1&k2");
        assertThat(QueryParams.parse("k1=v1&").toString()).isEqualTo("k1=v1&");
    }
}
