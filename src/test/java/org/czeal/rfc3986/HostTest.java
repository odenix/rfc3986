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


import static org.czeal.rfc3986.TestUtils.assertThrowsIAE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThat;
import static org.czeal.rfc3986.HostType.IPV4;
import static org.czeal.rfc3986.HostType.IPV6;
import static org.czeal.rfc3986.HostType.IPVFUTURE;
import static org.czeal.rfc3986.HostType.REGNAME;
import org.junit.jupiter.api.Test;


class HostTest
{
    @Test
    void parse()
    {
        assertDoesNotThrow(() -> Host.parse("example.com"));
        assertDoesNotThrow(() -> Host.parse("101.102.103.104"));
        assertDoesNotThrow(() -> Host.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]"));
        assertDoesNotThrow(() -> Host.parse("[2001:db8:0:1:1:1:1:1]"));
        assertDoesNotThrow(() -> Host.parse("[2001:0:9d38:6abd:0:0:0:42]"));
        assertDoesNotThrow(() -> Host.parse("[fe80::1]"));
        assertDoesNotThrow(() -> Host.parse("[2001:0:3238:DFE1:63::FEFB]"));
        assertDoesNotThrow(() -> Host.parse("[v1.fe80::a+en1]"));
        assertDoesNotThrow(() -> Host.parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D"));
        assertDoesNotThrow(() -> Host.parse(""));
        assertDoesNotThrow(() -> Host.parse(null));

        assertThrowsIAE(
            "The host value \"例子.测试\" has an invalid character \"例\" at the index 0.",
            () -> Authority.parse("例子.测试"));

        assertThrowsIAE(
            "The host value \"%XX\" has an invalid hex digit \"X\" at the index 1.",
            () -> Authority.parse("%XX"));
    }


    @Test
    void getType()
    {
        assertThat(Host.parse("example.com").getType()).isEqualTo(REGNAME);
        assertThat(Host.parse("101.102.103.104").getType()).isEqualTo(IPV4);
        assertThat(Host.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").getType()).isEqualTo(IPV6);
        assertThat(Host.parse("[2001:db8:0:1:1:1:1:1]").getType()).isEqualTo(IPV6);
        assertThat(Host.parse("[2001:0:9d38:6abd:0:0:0:42]").getType()).isEqualTo(IPV6);
        assertThat(Host.parse("[fe80::1]").getType()).isEqualTo(IPV6);
        assertThat(Host.parse("[2001:0:3238:DFE1:63::FEFB]").getType()).isEqualTo(IPV6);
        assertThat(Host.parse("[v1.fe80::a+en1]").getType()).isEqualTo(IPVFUTURE);
        assertThat(Host.parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D").getType()).isEqualTo(REGNAME);
        assertThat(Host.parse("").getType()).isEqualTo(REGNAME);
        assertThat(Host.parse(null).getType()).isEqualTo(REGNAME);
    }


    @Test
    void getValue()
    {
        assertThat(Host.parse("example.com").getValue()).isEqualTo("example.com");
        assertThat(Host.parse("101.102.103.104").getValue()).isEqualTo("101.102.103.104");
        assertThat(Host.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").getValue()).isEqualTo("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
        assertThat(Host.parse("[2001:db8:0:1:1:1:1:1]").getValue()).isEqualTo("[2001:db8:0:1:1:1:1:1]");
        assertThat(Host.parse("[2001:0:9d38:6abd:0:0:0:42]").getValue()).isEqualTo("[2001:0:9d38:6abd:0:0:0:42]");
        assertThat(Host.parse("[fe80::1]").getValue()).isEqualTo("[fe80::1]");
        assertThat(Host.parse("[2001:0:3238:DFE1:63::FEFB]").getValue()).isEqualTo("[2001:0:3238:DFE1:63::FEFB]");
        assertThat(Host.parse("[v1.fe80::a+en1]").getValue()).isEqualTo("[v1.fe80::a+en1]");
        assertThat(Host.parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D").getValue()).isEqualTo("%65%78%61%6D%70%6C%65%2E%63%6F%6D");
        assertThat(Host.parse("").getValue()).isEqualTo("");
        assertThat(Host.parse(null).getValue()).isEqualTo((String)null);
    }
}
