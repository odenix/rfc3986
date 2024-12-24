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
import static org.czeal.rfc3986.TestUtils.assertThrowsNPE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;


class QueryParamTest
{
    @Test
    void parse()
    {
        assertDoesNotThrow(() -> QueryParam.parse("k=v"));
        assertDoesNotThrow(() -> QueryParam.parse("k="));
        assertDoesNotThrow(() -> QueryParam.parse("=v"));
        assertDoesNotThrow(() -> QueryParam.parse("k"));
        assertDoesNotThrow(() -> QueryParam.parse(""));
        assertDoesNotThrow(() -> QueryParam.parse("k=v=v"));
        assertThrowsNPE("The input string must not be null.", () -> QueryParam.parse(null));
    }


    @Test
    void to_string()
    {
        assertThat(QueryParam.parse("k=v").toString()).isEqualTo("k=v");
        assertThat(QueryParam.parse("k=").toString()).isEqualTo("k=");
        assertThat(QueryParam.parse("=v").toString()).isEqualTo("=v");
        assertThat(QueryParam.parse("k").toString()).isEqualTo("k");
        assertThat(QueryParam.parse("").toString()).isEqualTo("");
        assertThat(QueryParam.parse("k=v=v").toString()).isEqualTo("k=v=v");
    }


    @Test
    void get_key()
    {
        assertThat(QueryParam.parse("k=v").getKey()).isEqualTo("k");
        assertThat(QueryParam.parse("k=").getKey()).isEqualTo("k");
        assertThat(QueryParam.parse("=v").getKey()).isEqualTo("");
        assertThat(QueryParam.parse("k").getKey()).isEqualTo("k");
        assertThat(QueryParam.parse("").getKey()).isEqualTo("");
        assertThat(QueryParam.parse("k=v=v").getKey()).isEqualTo("k");
    }


    @Test
    void get_value()
    {
        assertThat(QueryParam.parse("k=v").getValue()).isEqualTo("v");
        assertThat(QueryParam.parse("k=").getValue()).isEqualTo("");
        assertThat(QueryParam.parse("=v").getValue()).isEqualTo("v");
        assertThat(QueryParam.parse("k").getValue()).isEqualTo((String)null);
        assertThat(QueryParam.parse("").getValue()).isEqualTo((String)null);
        assertThat(QueryParam.parse("k=v=v").getValue()).isEqualTo("v=v");
    }
}
