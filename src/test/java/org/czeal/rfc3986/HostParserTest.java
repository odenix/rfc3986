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


import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.czeal.rfc3986.TestUtils.assertThrowsIAE;
import static org.czeal.rfc3986.HostType.IPV4;
import static org.czeal.rfc3986.HostType.IPV6;
import static org.czeal.rfc3986.HostType.IPVFUTURE;
import static org.czeal.rfc3986.HostType.REGNAME;
import org.junit.jupiter.api.Test;


class HostParserTest
{
    @Test
    void parse()
    {
        var host1 = new HostParser().parse("example.com", UTF_8);
        assertThat(host1.getType()).isEqualTo(REGNAME);
        assertThat(host1.getValue()).isEqualTo("example.com");

        var host2 = new HostParser().parse("101.102.103.104", UTF_8);
        assertThat(host2.getType()).isEqualTo(IPV4);
        assertThat(host2.getValue()).isEqualTo("101.102.103.104");

        var host3 = new HostParser().parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", UTF_8);
        assertThat(host3.getType()).isEqualTo(IPV6);
        assertThat(host3.getValue()).isEqualTo("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");

        var host4 = new HostParser().parse("[2001:db8:0:1:1:1:1:1]", UTF_8);
        assertThat(host4.getType()).isEqualTo(IPV6);
        assertThat(host4.getValue()).isEqualTo("[2001:db8:0:1:1:1:1:1]");

        var host5 = new HostParser().parse("[2001:0:9d38:6abd:0:0:0:42]", UTF_8);
        assertThat(host5.getType()).isEqualTo(IPV6);
        assertThat(host5.getValue()).isEqualTo("[2001:0:9d38:6abd:0:0:0:42]");

        var host6 = new HostParser().parse("[fe80::1]", UTF_8);
        assertThat(host6.getType()).isEqualTo(IPV6);
        assertThat(host6.getValue()).isEqualTo("[fe80::1]");

        var host7 = new HostParser().parse("[2001:0:3238:DFE1:63::FEFB]", UTF_8);
        assertThat(host7.getType()).isEqualTo(IPV6);
        assertThat(host7.getValue()).isEqualTo("[2001:0:3238:DFE1:63::FEFB]");

        Host host8 = new HostParser().parse("[v1.fe80::a+en1]", UTF_8);
        assertThat(host8.getType()).isEqualTo(IPVFUTURE);
        assertThat(host8.getValue()).isEqualTo("[v1.fe80::a+en1]");

        var host9 = new HostParser().parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D", UTF_8);
        assertThat(host9.getType()).isEqualTo(REGNAME);
        assertThat(host9.getValue()).isEqualTo("%65%78%61%6D%70%6C%65%2E%63%6F%6D");

        var host10 = new HostParser().parse("", UTF_8);
        assertThat(host10.getType()).isEqualTo(REGNAME);
        assertThat(host10.getValue()).isEqualTo("");

        var host11 = new HostParser().parse(null, UTF_8);
        assertThat(host11.getType()).isEqualTo(REGNAME);
        assertThat(host11.getValue()).isNull();

        assertThrowsIAE(
            "The host value \"例子.测试\" has an invalid character \"例\" at the index 0.",
            () -> new HostParser().parse("例子.测试", UTF_8));

        assertThrowsIAE(
            "The host value \"%XX\" has an invalid hex digit \"X\" at the index 1.",
            () -> new HostParser().parse("%XX", UTF_8));
    }
}
