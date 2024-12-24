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


class AuthorityBuilderTest
{
    @Test
    void build()
    {
        var authority1 = new AuthorityBuilder()
            .setCharset(UTF_8).setUserinfo("john").setHost("example.com").setPort(80).build();
        assertThat(authority1.getUserinfo()).isEqualTo("john");
        assertThat(authority1.getHost().getType()).isEqualTo(REGNAME);
        assertThat(authority1.getHost().getValue()).isEqualTo("example.com");
        assertThat(authority1.getPort()).isEqualTo(80);
        assertThat(authority1.toString()).isEqualTo("john@example.com:80");

        var authority2 = new AuthorityBuilder()
            .setCharset(UTF_8).setHost("example.com").setPort(80).build();
        assertThat(authority2.getUserinfo()).isNull();
        assertThat(authority2.getHost().getType()).isEqualTo(REGNAME);
        assertThat(authority2.getHost().getValue()).isEqualTo("example.com");
        assertThat(authority2.getPort()).isEqualTo(80);
        assertThat(authority2.toString()).isEqualTo("example.com:80");

        var authority3 = new AuthorityBuilder()
            .setCharset(UTF_8).setPort(80).build();
        assertThat(authority3.getUserinfo()).isNull();
        assertThat(authority3.getHost().getType()).isEqualTo(REGNAME);
        assertThat(authority3.getHost().getValue()).isNull();
        assertThat(authority3.getPort()).isEqualTo(80);
        assertThat(authority3.toString()).isEqualTo(":80");

        var authority4 = new AuthorityBuilder()
            .setCharset(UTF_8).build();
        assertThat(authority4.getUserinfo()).isNull();
        assertThat(authority3.getHost().getType()).isEqualTo(REGNAME);
        assertThat(authority3.getHost().getValue()).isNull();
        assertThat(authority4.getPort()).isEqualTo(-1);
        assertThat(authority4.toString()).isEqualTo("");

        var authority5 = new AuthorityBuilder()
            .setCharset(UTF_8).setUserinfo("john").setHost("101.102.103.104").setPort(80).build();
        assertThat(authority5.getUserinfo()).isEqualTo("john");
        assertThat(authority5.getHost().getType()).isEqualTo(IPV4);
        assertThat(authority5.getHost().getValue()).isEqualTo("101.102.103.104");
        assertThat(authority5.getPort()).isEqualTo(80);
        assertThat(authority5.toString()).isEqualTo("john@101.102.103.104:80");

        var authority6 = new AuthorityBuilder()
            .setCharset(UTF_8).setUserinfo("john").setHost("101.102.103.104").setPort(80).build();
        assertThat(authority6.getUserinfo()).isEqualTo("john");
        assertThat(authority6.getHost().getType()).isEqualTo(IPV4);
        assertThat(authority6.getHost().getValue()).isEqualTo("101.102.103.104");
        assertThat(authority6.getPort()).isEqualTo(80);
        assertThat(authority6.toString()).isEqualTo("john@101.102.103.104:80");

        var authority7 = new AuthorityBuilder()
            .setCharset(UTF_8).setUserinfo("john").setHost("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").setPort(80).build();
        assertThat(authority7.getUserinfo()).isEqualTo("john");
        assertThat(authority7.getHost().getType()).isEqualTo(IPV6);
        assertThat(authority7.getHost().getValue()).isEqualTo("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
        assertThat(authority7.getPort()).isEqualTo(80);
        assertThat(authority7.toString()).isEqualTo("john@[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]:80");

        var authority8 = new AuthorityBuilder()
            .setCharset(UTF_8).setUserinfo("john").setHost("[v1.fe80::a+en1]").setPort(80).build();
        assertThat(authority8.getUserinfo()).isEqualTo("john");
        assertThat(authority8.getHost().getType()).isEqualTo(IPVFUTURE);
        assertThat(authority8.getHost().getValue()).isEqualTo("[v1.fe80::a+en1]");
        assertThat(authority8.getPort()).isEqualTo(80);
        assertThat(authority8.toString()).isEqualTo("john@[v1.fe80::a+en1]:80");

        assertThrowsIAE(
            "The userinfo value \"?\" has an invalid character \"?\" at the index 0.",
            () -> new AuthorityBuilder().setCharset(UTF_8).setUserinfo("?").build());

        assertThrowsIAE(
            "The userinfo value \"%XX\" has an invalid hex digit \"X\" at the index 1.",
            () -> new AuthorityBuilder().setCharset(UTF_8).setUserinfo("%XX").build());
    }
}
