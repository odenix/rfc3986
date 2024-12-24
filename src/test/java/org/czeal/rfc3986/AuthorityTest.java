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


class AuthorityTest
{
    @Test
    void parse()
    {
        assertDoesNotThrow(() -> Authority.parse("example.com"));
        assertDoesNotThrow(() -> Authority.parse("john@example.com:80"));
        assertDoesNotThrow(() -> Authority.parse("example.com:001"));
        assertDoesNotThrow(() -> Authority.parse("%6A%6F%68%6E@example.com"));
        assertDoesNotThrow(() -> Authority.parse("101.102.103.104"));
        assertDoesNotThrow(() -> Authority.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]"));
        assertDoesNotThrow(() -> Authority.parse("[2001:db8:0:1:1:1:1:1]"));
        assertDoesNotThrow(() -> Authority.parse("[2001:0:9d38:6abd:0:0:0:42]"));
        assertDoesNotThrow(() -> Authority.parse("[fe80::1]"));
        assertDoesNotThrow(() -> Authority.parse("[2001:0:3238:DFE1:63::FEFB]"));
        assertDoesNotThrow(() -> Authority.parse("[v1.fe80::a+en1]"));
        assertDoesNotThrow(() -> Authority.parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D"));
        assertDoesNotThrow(() -> Authority.parse(""));
        assertDoesNotThrow(() -> Authority.parse(null));

        assertThrowsIAE(
            "The port value \"password@example.com\" has an invalid character \"p\" at the index 0.",
            () -> Authority.parse("user@name:password@example.com"));

        assertThrowsIAE(
            "The userinfo value \"müller\" has an invalid character \"ü\" at the index 1.",
            () -> Authority.parse("müller@example.com"));

        assertThrowsIAE(
            "The host value \"%XX\" has an invalid hex digit \"X\" at the index 1.",
            () -> Authority.parse("%XX"));
    }


    @Test
    void get_user_info()
    {
        assertThat(Authority.parse("example.com").getUserinfo()).isNull();
        assertThat(Authority.parse("john@example.com:80").getUserinfo()).isEqualTo("john");
        assertThat(Authority.parse("example.com:001").getUserinfo()).isNull();
        assertThat(Authority.parse("%6A%6F%68%6E@example.com").getUserinfo()).isEqualTo("%6A%6F%68%6E");
        assertThat(Authority.parse("101.102.103.104").getUserinfo()).isNull();
        assertThat(Authority.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").getUserinfo()).isNull();
        assertThat(Authority.parse("[2001:db8:0:1:1:1:1:1]").getUserinfo()).isNull();
        assertThat(Authority.parse("[2001:0:9d38:6abd:0:0:0:42]").getUserinfo()).isNull();
        assertThat(Authority.parse("[fe80::1]").getUserinfo()).isNull();
        assertThat(Authority.parse("[2001:0:3238:DFE1:63::FEFB]").getUserinfo()).isNull();
        assertThat(Authority.parse("[v1.fe80::a+en1]").getUserinfo()).isNull();
        assertThat(Authority.parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D").getUserinfo()).isNull();
        assertThat(Authority.parse("").getUserinfo()).isNull();
    }


    @Test
    void get_host()
    {
        assertThat(Authority.parse("example.com").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(Authority.parse("john@example.com:80").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(Authority.parse("example.com:001").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(Authority.parse("%6A%6F%68%6E@example.com").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(Authority.parse("101.102.103.104").getHost()).isEqualTo(new Host(IPV4, "101.102.103.104"));
        assertThat(Authority.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").getHost()).isEqualTo(new Host(IPV6, "[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]"));
        assertThat(Authority.parse("[2001:db8:0:1:1:1:1:1]").getHost()).isEqualTo(new Host(IPV6, "[2001:db8:0:1:1:1:1:1]"));
        assertThat(Authority.parse("[2001:0:9d38:6abd:0:0:0:42]").getHost()).isEqualTo(new Host(IPV6, "[2001:0:9d38:6abd:0:0:0:42]"));
        assertThat(Authority.parse("[fe80::1]").getHost()).isEqualTo(new Host(IPV6, "[fe80::1]"));
        assertThat(Authority.parse("[2001:0:3238:DFE1:63::FEFB]").getHost()).isEqualTo(new Host(IPV6, "[2001:0:3238:DFE1:63::FEFB]"));
        assertThat(Authority.parse("[v1.fe80::a+en1]").getHost()).isEqualTo(new Host(IPVFUTURE, "[v1.fe80::a+en1]"));
        assertThat(Authority.parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D").getHost()).isEqualTo(new Host(REGNAME, "%65%78%61%6D%70%6C%65%2E%63%6F%6D"));
        assertThat(Authority.parse("").getHost()).isEqualTo(new Host(REGNAME, ""));
    }


    @Test
    void get_port()
    {
        assertThat(Authority.parse("example.com").getPort()).isEqualTo(-1);
        assertThat(Authority.parse("john@example.com:80").getPort()).isEqualTo(80);
        assertThat(Authority.parse("example.com:001").getPort()).isEqualTo(1);
        assertThat(Authority.parse("%6A%6F%68%6E@example.com").getPort()).isEqualTo(-1);
        assertThat(Authority.parse("101.102.103.104").getPort()).isEqualTo(-1);
        assertThat(Authority.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").getPort()).isEqualTo(-1);
        assertThat(Authority.parse("[2001:db8:0:1:1:1:1:1]").getPort()).isEqualTo(-1);
        assertThat(Authority.parse("[2001:0:9d38:6abd:0:0:0:42]").getPort()).isEqualTo(-1);
        assertThat(Authority.parse("[fe80::1]").getPort()).isEqualTo(-1);
        assertThat(Authority.parse("[2001:0:3238:DFE1:63::FEFB]").getPort()).isEqualTo(-1);
        assertThat(Authority.parse("[v1.fe80::a+en1]").getPort()).isEqualTo(-1);
        assertThat(Authority.parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D").getPort()).isEqualTo(-1);
        assertThat(Authority.parse("").getPort()).isEqualTo(-1);
    }


    @Test
    void to_string()
    {
        assertThat(Authority.parse("example.com").toString()).isEqualTo("example.com");
        assertThat(Authority.parse("john@example.com:80").toString()).isEqualTo("john@example.com:80");
        assertThat(Authority.parse("example.com:001").toString()).isEqualTo("example.com:1");
        assertThat(Authority.parse("%6A%6F%68%6E@example.com").toString()).isEqualTo("%6A%6F%68%6E@example.com");
        assertThat(Authority.parse("101.102.103.104").toString()).isEqualTo("101.102.103.104");
        assertThat(Authority.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").toString()).isEqualTo("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
        assertThat(Authority.parse("[2001:db8:0:1:1:1:1:1]").toString()).isEqualTo("[2001:db8:0:1:1:1:1:1]");
        assertThat(Authority.parse("[2001:0:9d38:6abd:0:0:0:42]").toString()).isEqualTo("[2001:0:9d38:6abd:0:0:0:42]");
        assertThat(Authority.parse("[fe80::1]").toString()).isEqualTo("[fe80::1]");
        assertThat(Authority.parse("[2001:0:3238:DFE1:63::FEFB]").toString()).isEqualTo("[2001:0:3238:DFE1:63::FEFB]");
        assertThat(Authority.parse("[v1.fe80::a+en1]").toString()).isEqualTo("[v1.fe80::a+en1]");
        assertThat(Authority.parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D").toString()).isEqualTo("%65%78%61%6D%70%6C%65%2E%63%6F%6D");
        assertThat(Authority.parse("").toString()).isEqualTo("");
    }
}
