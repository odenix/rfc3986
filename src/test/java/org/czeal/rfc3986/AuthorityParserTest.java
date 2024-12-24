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


class AuthorityParserTest
{
    @Test
    void parse()
    {
        var authority1 = new AuthorityParser().parse("example.com", UTF_8);
        assertThat(authority1.getUserinfo()).isNull();
        assertThat(authority1.getHost().getType()).isEqualTo(REGNAME);
        assertThat(authority1.getHost().getValue()).isEqualTo("example.com");
        assertThat(authority1.getPort()).isEqualTo(-1);
        assertThat(authority1.toString()).isEqualTo("example.com");

        var authority2 = new AuthorityParser().parse("john@example.com:80", UTF_8);
        assertThat(authority2.getUserinfo()).isEqualTo("john");
        assertThat(authority2.getHost().getType()).isEqualTo(REGNAME);
        assertThat(authority2.getHost().getValue()).isEqualTo("example.com");
        assertThat(authority2.getPort()).isEqualTo(80);
        assertThat(authority2.toString()).isEqualTo("john@example.com:80");

        var authority3 = new AuthorityParser().parse("example.com:001", UTF_8);
        assertThat(authority3.getUserinfo()).isNull();
        assertThat(authority3.getHost().getType()).isEqualTo(REGNAME);
        assertThat(authority3.getHost().getValue()).isEqualTo("example.com");
        assertThat(authority3.getPort()).isEqualTo(1);
        assertThat(authority3.toString()).isEqualTo("example.com:1");

        var authority4 = new AuthorityParser().parse("%6A%6F%68%6E@example.com", UTF_8);
        assertThat(authority4.getUserinfo()).isEqualTo("%6A%6F%68%6E");
        assertThat(authority4.getHost().getType()).isEqualTo(REGNAME);
        assertThat(authority4.getHost().getValue()).isEqualTo("example.com");
        assertThat(authority4.getPort()).isEqualTo(-1);
        assertThat(authority4.toString()).isEqualTo("%6A%6F%68%6E@example.com");

        var authority5 = new AuthorityParser().parse("101.102.103.104", UTF_8);
        assertThat(authority5.getUserinfo()).isNull();
        assertThat(authority5.getHost().getType()).isEqualTo(IPV4);
        assertThat(authority5.getHost().getValue()).isEqualTo("101.102.103.104");
        assertThat(authority5.getPort()).isEqualTo(-1);
        assertThat(authority5.toString()).isEqualTo("101.102.103.104");

        var authority6 = new AuthorityParser().parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", UTF_8);
        assertThat(authority6.getUserinfo()).isNull();
        assertThat(authority6.getHost().getType()).isEqualTo(IPV6);
        assertThat(authority6.getHost().getValue()).isEqualTo("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
        assertThat(authority6.getPort()).isEqualTo(-1);
        assertThat(authority6.toString()).isEqualTo("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");

        var authority7 = new AuthorityParser().parse("[2001:db8:0:1:1:1:1:1]", UTF_8);
        assertThat(authority7.getUserinfo()).isNull();
        assertThat(authority7.getHost().getType()).isEqualTo(IPV6);
        assertThat(authority7.getHost().getValue()).isEqualTo("[2001:db8:0:1:1:1:1:1]");
        assertThat(authority7.getPort()).isEqualTo(-1);
        assertThat(authority7.toString()).isEqualTo("[2001:db8:0:1:1:1:1:1]");

        var authority8 = new AuthorityParser().parse("[2001:db8:0:1:1:1:1:1]", UTF_8);
        assertThat(authority8.getUserinfo()).isNull();
        assertThat(authority8.getHost().getType()).isEqualTo(IPV6);
        assertThat(authority8.getHost().getValue()).isEqualTo("[2001:db8:0:1:1:1:1:1]");
        assertThat(authority8.getPort()).isEqualTo(-1);
        assertThat(authority8.toString()).isEqualTo("[2001:db8:0:1:1:1:1:1]");

        var authority9 = new AuthorityParser().parse("[2001:0:9d38:6abd:0:0:0:42]", UTF_8);
        assertThat(authority9.getUserinfo()).isNull();
        assertThat(authority9.getHost().getType()).isEqualTo(IPV6);
        assertThat(authority9.getHost().getValue()).isEqualTo("[2001:0:9d38:6abd:0:0:0:42]");
        assertThat(authority9.getPort()).isEqualTo(-1);
        assertThat(authority9.toString()).isEqualTo("[2001:0:9d38:6abd:0:0:0:42]");

        var authority10 = new AuthorityParser().parse("[fe80::1]", UTF_8);
        assertThat(authority10.getUserinfo()).isNull();
        assertThat(authority10.getHost().getType()).isEqualTo(IPV6);
        assertThat(authority10.getHost().getValue()).isEqualTo("[fe80::1]");
        assertThat(authority10.getPort()).isEqualTo(-1);
        assertThat(authority10.toString()).isEqualTo("[fe80::1]");

        var authority11 = new AuthorityParser().parse("[2001:0:3238:DFE1:63::FEFB]", UTF_8);
        assertThat(authority11.getUserinfo()).isNull();
        assertThat(authority11.getHost().getType()).isEqualTo(IPV6);
        assertThat(authority11.getHost().getValue()).isEqualTo("[2001:0:3238:DFE1:63::FEFB]");
        assertThat(authority11.getPort()).isEqualTo(-1);
        assertThat(authority11.toString()).isEqualTo("[2001:0:3238:DFE1:63::FEFB]");

        var authority12 = new AuthorityParser().parse("[v1.fe80::a+en1]", UTF_8);
        assertThat(authority12.getUserinfo()).isNull();
        assertThat(authority12.getHost().getType()).isEqualTo(IPVFUTURE);
        assertThat(authority12.getHost().getValue()).isEqualTo("[v1.fe80::a+en1]");
        assertThat(authority12.getPort()).isEqualTo(-1);
        assertThat(authority12.toString()).isEqualTo("[v1.fe80::a+en1]");

        var authority13 = new AuthorityParser().parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D", UTF_8);
        assertThat(authority13.getUserinfo()).isNull();
        assertThat(authority13.getHost().getType()).isEqualTo(REGNAME);
        assertThat(authority13.getHost().getValue()).isEqualTo("%65%78%61%6D%70%6C%65%2E%63%6F%6D");
        assertThat(authority13.getPort()).isEqualTo(-1);
        assertThat(authority13.toString()).isEqualTo("%65%78%61%6D%70%6C%65%2E%63%6F%6D");

        var authority14 = new AuthorityParser().parse("", UTF_8);
        assertThat(authority14.getUserinfo()).isNull();
        assertThat(authority14.getHost().getType()).isEqualTo(REGNAME);
        assertThat(authority14.getHost().getValue()).isEqualTo("");
        assertThat(authority14.getPort()).isEqualTo(-1);
        assertThat(authority14.toString()).isEqualTo("");

        assertThrowsIAE(
            "The port value \"password@example.com\" has an invalid character \"p\" at the index 0.",
            () -> Authority.parse("user@name:password@example.com", UTF_8));

        assertThrowsIAE(
            "The userinfo value \"müller\" has an invalid character \"ü\" at the index 1.",
            () -> Authority.parse("müller@example.com", UTF_8));

        assertThrowsIAE(
            "The host value \"%XX\" has an invalid hex digit \"X\" at the index 1.",
            () -> Authority.parse("%XX", UTF_8));
    }
}
