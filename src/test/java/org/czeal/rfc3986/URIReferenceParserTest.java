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
import static org.czeal.rfc3986.HostType.IPV6;
import static org.czeal.rfc3986.HostType.IPVFUTURE;
import static org.czeal.rfc3986.HostType.REGNAME;
import static org.czeal.rfc3986.TestUtils.assertThrowsIAE;

import org.junit.jupiter.api.Test;


class URIReferenceParserTest
{
    @Test
    void parse()
    {
        var uriRef1 = new URIReferenceParser().parse("http://example.com", UTF_8);
        assertThat(uriRef1.toString()).isEqualTo("http://example.com");
        assertThat(uriRef1.isRelativeReference()).isFalse();
        assertThat(uriRef1.getScheme()).isEqualTo("http");
        assertThat(uriRef1.hasAuthority()).isTrue();
        assertThat(uriRef1.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef1.getUserinfo()).isNull();
        assertThat(uriRef1.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef1.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef1.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef1.getPort()).isEqualTo(-1);
        assertThat(uriRef1.getPath()).isEqualTo("");
        assertThat(uriRef1.getQuery()).isNull();
        assertThat(uriRef1.getFragment()).isNull();

        var uriRef2 = new URIReferenceParser().parse("hTTp://example.com", UTF_8);
        assertThat(uriRef2.toString()).isEqualTo("hTTp://example.com");
        assertThat(uriRef2.isRelativeReference()).isFalse();
        assertThat(uriRef2.getScheme()).isEqualTo("hTTp");
        assertThat(uriRef2.hasAuthority()).isTrue();
        assertThat(uriRef2.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef2.getUserinfo()).isNull();
        assertThat(uriRef2.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef2.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef2.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef2.getPort()).isEqualTo(-1);
        assertThat(uriRef2.getPath()).isEqualTo("");
        assertThat(uriRef2.getQuery()).isNull();
        assertThat(uriRef2.getFragment()).isNull();

        var uriRef3 = new URIReferenceParser().parse("//example.com", UTF_8);
        assertThat(uriRef3.toString()).isEqualTo("//example.com");
        assertThat(uriRef3.isRelativeReference()).isTrue();
        assertThat(uriRef3.getScheme()).isNull();
        assertThat(uriRef3.hasAuthority()).isTrue();
        assertThat(uriRef3.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef3.getUserinfo()).isNull();
        assertThat(uriRef3.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef3.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef3.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef3.getPort()).isEqualTo(-1);
        assertThat(uriRef3.getPath()).isEqualTo("");
        assertThat(uriRef3.getQuery()).isNull();
        assertThat(uriRef3.getFragment()).isNull();

        var uriRef4 = new URIReferenceParser().parse("http:", UTF_8);
        assertThat(uriRef4.isRelativeReference()).isFalse();
        assertThat(uriRef4.getScheme()).isEqualTo("http");
        assertThat(uriRef4.hasAuthority()).isFalse();
        assertThat(uriRef4.getAuthority()).isNull();
        assertThat(uriRef4.getUserinfo()).isNull();
        assertThat(uriRef4.getHost()).isNull();
        assertThat(uriRef4.getPort()).isEqualTo(-1);
        assertThat(uriRef4.getPath()).isEqualTo("");
        assertThat(uriRef4.getQuery()).isNull();
        assertThat(uriRef4.getFragment()).isNull();

        var uriRef5 = new URIReferenceParser().parse("http://john@example.com", UTF_8);
        assertThat(uriRef5.toString()).isEqualTo("http://john@example.com");
        assertThat(uriRef5.isRelativeReference()).isFalse();
        assertThat(uriRef5.getScheme()).isEqualTo("http");
        assertThat(uriRef5.hasAuthority()).isTrue();
        assertThat(uriRef5.getAuthority().toString()).isEqualTo("john@example.com");
        assertThat(uriRef5.getUserinfo()).isEqualTo("john");
        assertThat(uriRef5.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef5.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef5.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef5.getPort()).isEqualTo(-1);
        assertThat(uriRef5.getPath()).isEqualTo("");
        assertThat(uriRef5.getQuery()).isNull();
        assertThat(uriRef5.getFragment()).isNull();

        var uriRef6 = new URIReferenceParser().parse("http://%6A%6F%68%6E@example.com", UTF_8);
        assertThat(uriRef6.toString()).isEqualTo("http://%6A%6F%68%6E@example.com");
        assertThat(uriRef6.isRelativeReference()).isFalse();
        assertThat(uriRef6.getScheme()).isEqualTo("http");
        assertThat(uriRef6.hasAuthority()).isTrue();
        assertThat(uriRef6.getAuthority().toString()).isEqualTo("%6A%6F%68%6E@example.com");
        assertThat(uriRef6.getUserinfo()).isEqualTo("%6A%6F%68%6E");
        assertThat(uriRef6.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef6.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef6.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef6.getPort()).isEqualTo(-1);
        assertThat(uriRef6.getPath()).isEqualTo("");
        assertThat(uriRef6.getQuery()).isNull();
        assertThat(uriRef6.getFragment()).isNull();

        var uriRef7 = new URIReferenceParser().parse("http://101.102.103.104", UTF_8);
        assertThat(uriRef7.toString()).isEqualTo("http://101.102.103.104");
        assertThat(uriRef7.isRelativeReference()).isFalse();
        assertThat(uriRef7.getScheme()).isEqualTo("http");
        assertThat(uriRef7.hasAuthority()).isTrue();
        assertThat(uriRef7.getAuthority().toString()).isEqualTo("101.102.103.104");
        assertThat(uriRef7.getUserinfo()).isNull();
        assertThat(uriRef7.getHost().toString()).isEqualTo("101.102.103.104");
        assertThat(uriRef7.getHost().getValue()).isEqualTo("101.102.103.104");
        assertThat(uriRef7.getHost().getType()).isEqualTo(HostType.IPV4);
        assertThat(uriRef7.getPort()).isEqualTo(-1);
        assertThat(uriRef7.getPath()).isEqualTo("");
        assertThat(uriRef7.getQuery()).isNull();
        assertThat(uriRef7.getFragment()).isNull();

        var uriRef8 = new URIReferenceParser().parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", UTF_8);
        assertThat(uriRef8.toString()).isEqualTo("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
        assertThat(uriRef8.isRelativeReference()).isFalse();
        assertThat(uriRef8.getScheme()).isEqualTo("http");
        assertThat(uriRef8.hasAuthority()).isTrue();
        assertThat(uriRef8.getAuthority().toString()).isEqualTo("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
        assertThat(uriRef8.getAuthority().getUserinfo()).isNull();
        assertThat(uriRef8.getHost().toString()).isEqualTo("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
        assertThat(uriRef8.getHost().getValue()).isEqualTo("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
        assertThat(uriRef8.getHost().getType()).isEqualTo(IPV6);
        assertThat(uriRef8.getPort()).isEqualTo(-1);
        assertThat(uriRef8.getPath()).isEqualTo("");
        assertThat(uriRef8.getQuery()).isNull();
        assertThat(uriRef8.getFragment()).isNull();

        var uriRef9 = new URIReferenceParser().parse("http://[2001:db8:0:1:1:1:1:1]", UTF_8);
        assertThat(uriRef9.toString()).isEqualTo("http://[2001:db8:0:1:1:1:1:1]");
        assertThat(uriRef9.isRelativeReference()).isFalse();
        assertThat(uriRef9.getScheme()).isEqualTo("http");
        assertThat(uriRef9.hasAuthority()).isTrue();
        assertThat(uriRef9.getAuthority().toString()).isEqualTo("[2001:db8:0:1:1:1:1:1]");
        assertThat(uriRef9.getUserinfo()).isNull();
        assertThat(uriRef9.getHost().toString()).isEqualTo("[2001:db8:0:1:1:1:1:1]");
        assertThat(uriRef9.getHost().getValue()).isEqualTo("[2001:db8:0:1:1:1:1:1]");
        assertThat(uriRef9.getHost().getType()).isEqualTo(IPV6);
        assertThat(uriRef9.getPort()).isEqualTo(-1);
        assertThat(uriRef9.getPath()).isEqualTo("");
        assertThat(uriRef9.getQuery()).isNull();
        assertThat(uriRef9.getFragment()).isNull();

        var uriRef10 = new URIReferenceParser().parse("http://[2001:0:9d38:6abd:0:0:0:42]", UTF_8);
        assertThat(uriRef10.toString()).isEqualTo("http://[2001:0:9d38:6abd:0:0:0:42]");
        assertThat(uriRef10.isRelativeReference()).isFalse();
        assertThat(uriRef10.getScheme()).isEqualTo("http");
        assertThat(uriRef10.hasAuthority()).isTrue();
        assertThat(uriRef10.getAuthority().toString()).isEqualTo("[2001:0:9d38:6abd:0:0:0:42]");
        assertThat(uriRef10.getUserinfo()).isNull();
        assertThat(uriRef10.getHost().toString()).isEqualTo("[2001:0:9d38:6abd:0:0:0:42]");
        assertThat(uriRef10.getHost().getValue()).isEqualTo("[2001:0:9d38:6abd:0:0:0:42]");
        assertThat(uriRef10.getHost().getType()).isEqualTo(IPV6);
        assertThat(uriRef10.getPort()).isEqualTo(-1);
        assertThat(uriRef10.getPath()).isEqualTo("");
        assertThat(uriRef10.getQuery()).isNull();
        assertThat(uriRef10.getFragment()).isNull();

        var uriRef11 = new URIReferenceParser().parse("http://[fe80::1]", UTF_8);
        assertThat(uriRef11.toString()).isEqualTo("http://[fe80::1]");
        assertThat(uriRef11.isRelativeReference()).isFalse();
        assertThat(uriRef11.getScheme()).isEqualTo("http");
        assertThat(uriRef11.hasAuthority()).isTrue();
        assertThat(uriRef11.getAuthority().toString()).isEqualTo("[fe80::1]");
        assertThat(uriRef11.getUserinfo()).isNull();
        assertThat(uriRef11.getHost().toString()).isEqualTo("[fe80::1]");
        assertThat(uriRef11.getHost().getValue()).isEqualTo("[fe80::1]");
        assertThat(uriRef11.getHost().getType()).isEqualTo(IPV6);
        assertThat(uriRef11.getPort()).isEqualTo(-1);
        assertThat(uriRef11.getPath()).isEqualTo("");
        assertThat(uriRef11.getQuery()).isNull();
        assertThat(uriRef11.getFragment()).isNull();

        var uriRef12 = new URIReferenceParser().parse("http://[2001:0:3238:DFE1:63::FEFB]", UTF_8);
        assertThat(uriRef12.toString()).isEqualTo("http://[2001:0:3238:DFE1:63::FEFB]");
        assertThat(uriRef12.isRelativeReference()).isFalse();
        assertThat(uriRef12.getScheme()).isEqualTo("http");
        assertThat(uriRef12.hasAuthority()).isTrue();
        assertThat(uriRef12.getAuthority().toString()).isEqualTo("[2001:0:3238:DFE1:63::FEFB]");
        assertThat(uriRef12.getUserinfo()).isNull();
        assertThat(uriRef12.getHost().toString()).isEqualTo("[2001:0:3238:DFE1:63::FEFB]");
        assertThat(uriRef12.getHost().getValue()).isEqualTo("[2001:0:3238:DFE1:63::FEFB]");
        assertThat(uriRef12.getHost().getType()).isEqualTo(IPV6);
        assertThat(uriRef12.getPort()).isEqualTo(-1);
        assertThat(uriRef12.getPath()).isEqualTo("");
        assertThat(uriRef12.getQuery()).isNull();
        assertThat(uriRef12.getFragment()).isNull();

        var uriRef13 = new URIReferenceParser().parse("http://[v1.fe80::a+en1]", UTF_8);
        assertThat(uriRef13.toString()).isEqualTo("http://[v1.fe80::a+en1]");
        assertThat(uriRef13.isRelativeReference()).isFalse();
        assertThat(uriRef13.getScheme()).isEqualTo("http");
        assertThat(uriRef13.hasAuthority()).isTrue();
        assertThat(uriRef13.getAuthority().toString()).isEqualTo("[v1.fe80::a+en1]");
        assertThat(uriRef13.getUserinfo()).isNull();
        assertThat(uriRef13.getHost().toString()).isEqualTo("[v1.fe80::a+en1]");
        assertThat(uriRef13.getHost().getValue()).isEqualTo("[v1.fe80::a+en1]");
        assertThat(uriRef13.getHost().getType()).isEqualTo(IPVFUTURE);
        assertThat(uriRef13.getPort()).isEqualTo(-1);
        assertThat(uriRef13.getPath()).isEqualTo("");
        assertThat(uriRef13.getQuery()).isNull();
        assertThat(uriRef13.getFragment()).isNull();

        var uriRef14 = new URIReferenceParser().parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D", UTF_8);
        assertThat(uriRef14.toString()).isEqualTo("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D");
        assertThat(uriRef14.isRelativeReference()).isFalse();
        assertThat(uriRef14.getScheme()).isEqualTo("http");
        assertThat(uriRef14.hasAuthority()).isTrue();
        assertThat(uriRef14.getAuthority().toString()).isEqualTo("%65%78%61%6D%70%6C%65%2E%63%6F%6D");
        assertThat(uriRef14.getUserinfo()).isNull();
        assertThat(uriRef14.getHost().toString()).isEqualTo("%65%78%61%6D%70%6C%65%2E%63%6F%6D");
        assertThat(uriRef14.getHost().getValue()).isEqualTo("%65%78%61%6D%70%6C%65%2E%63%6F%6D");
        assertThat(uriRef14.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef14.getPort()).isEqualTo(-1);
        assertThat(uriRef14.getPath()).isEqualTo("");
        assertThat(uriRef14.getQuery()).isNull();
        assertThat(uriRef14.getFragment()).isNull();

        var uriRef15 = new URIReferenceParser().parse("http://", UTF_8);
        assertThat(uriRef15.isRelativeReference()).isFalse();
        assertThat(uriRef15.getScheme()).isEqualTo("http");
        assertThat(uriRef15.hasAuthority()).isTrue();
        assertThat(uriRef15.getAuthority().toString()).isEqualTo("");
        assertThat(uriRef15.getUserinfo()).isNull();
        assertThat(uriRef15.getHost().toString()).isEqualTo("");
        assertThat(uriRef15.getHost().getValue()).isEqualTo("");
        assertThat(uriRef15.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef15.getPort()).isEqualTo(-1);
        assertThat(uriRef15.getPath()).isEqualTo("");
        assertThat(uriRef15.getQuery()).isNull();
        assertThat(uriRef15.getFragment()).isNull();

        var uriRef16 = new URIReferenceParser().parse("http:///a", UTF_8);
        assertThat(uriRef16.isRelativeReference()).isFalse();
        assertThat(uriRef16.getScheme()).isEqualTo("http");
        assertThat(uriRef16.hasAuthority()).isTrue();
        assertThat(uriRef16.getAuthority().toString()).isEqualTo("");
        assertThat(uriRef16.getUserinfo()).isNull();
        assertThat(uriRef16.getHost().toString()).isEqualTo("");
        assertThat(uriRef16.getHost().getValue()).isEqualTo("");
        assertThat(uriRef16.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef16.getPort()).isEqualTo(-1);
        assertThat(uriRef16.getPath()).isEqualTo("/a");
        assertThat(uriRef16.getQuery()).isNull();
        assertThat(uriRef16.getFragment()).isNull();

        var uriRef17 = new URIReferenceParser().parse("http://example.com:80", UTF_8);
        assertThat(uriRef17.isRelativeReference()).isFalse();
        assertThat(uriRef17.getScheme()).isEqualTo("http");
        assertThat(uriRef17.hasAuthority()).isTrue();
        assertThat(uriRef17.getAuthority().toString()).isEqualTo("example.com:80");
        assertThat(uriRef17.getUserinfo()).isNull();
        assertThat(uriRef17.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef17.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef17.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef17.getPort()).isEqualTo(80);
        assertThat(uriRef17.getPath()).isEqualTo("");
        assertThat(uriRef17.getQuery()).isNull();
        assertThat(uriRef17.getFragment()).isNull();

        var uriRef18 = new URIReferenceParser().parse("http://example.com:", UTF_8);
        assertThat(uriRef18.isRelativeReference()).isFalse();
        assertThat(uriRef18.getScheme()).isEqualTo("http");
        assertThat(uriRef18.hasAuthority()).isTrue();
        assertThat(uriRef18.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef18.getUserinfo()).isNull();
        assertThat(uriRef18.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef18.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef18.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef18.getPort()).isEqualTo(-1);
        assertThat(uriRef18.getPath()).isEqualTo("");
        assertThat(uriRef18.getQuery()).isNull();
        assertThat(uriRef18.getFragment()).isNull();

        var uriRef19 = new URIReferenceParser().parse("http://example.com:001", UTF_8);
        assertThat(uriRef19.isRelativeReference()).isFalse();
        assertThat(uriRef19.getScheme()).isEqualTo("http");
        assertThat(uriRef19.hasAuthority()).isTrue();
        assertThat(uriRef19.getAuthority().toString()).isEqualTo("example.com:1");
        assertThat(uriRef19.getAuthority().getUserinfo()).isNull();
        assertThat(uriRef19.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef19.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef19.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef19.getPort()).isEqualTo(1);
        assertThat(uriRef19.getPath()).isEqualTo("");
        assertThat(uriRef19.getQuery()).isNull();
        assertThat(uriRef19.getFragment()).isNull();

        var uriRef20 = new URIReferenceParser().parse("http://example.com/a/b/c", UTF_8);
        assertThat(uriRef20.toString()).isEqualTo("http://example.com/a/b/c");
        assertThat(uriRef20.isRelativeReference()).isFalse();
        assertThat(uriRef20.getScheme()).isEqualTo("http");
        assertThat(uriRef20.hasAuthority()).isTrue();
        assertThat(uriRef20.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef20.getAuthority().getUserinfo()).isNull();
        assertThat(uriRef20.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef20.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef20.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef20.getPort()).isEqualTo(-1);
        assertThat(uriRef20.getPath()).isEqualTo("/a/b/c");
        assertThat(uriRef20.getQuery()).isNull();
        assertThat(uriRef20.getFragment()).isNull();

        var uriRef21 = new URIReferenceParser().parse("http://example.com/%61/%62/%63", UTF_8);
        assertThat(uriRef21.toString()).isEqualTo("http://example.com/%61/%62/%63");
        assertThat(uriRef21.isRelativeReference()).isFalse();
        assertThat(uriRef21.getScheme()).isEqualTo("http");
        assertThat(uriRef21.hasAuthority()).isTrue();
        assertThat(uriRef21.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef21.getUserinfo()).isNull();
        assertThat(uriRef21.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef21.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef21.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef21.getPort()).isEqualTo(-1);
        assertThat(uriRef21.getPath()).isEqualTo("/%61/%62/%63");
        assertThat(uriRef21.getQuery()).isNull();
        assertThat(uriRef21.getFragment()).isNull();

        var uriRef22 = new URIReferenceParser().parse("http:/a", UTF_8);
        assertThat(uriRef22.isRelativeReference()).isFalse();
        assertThat(uriRef22.getScheme()).isEqualTo("http");
        assertThat(uriRef22.hasAuthority()).isFalse();
        assertThat(uriRef22.getAuthority()).isNull();
        assertThat(uriRef22.getUserinfo()).isNull();
        assertThat(uriRef22.getHost()).isNull();
        assertThat(uriRef22.getPort()).isEqualTo(-1);
        assertThat(uriRef22.getPath()).isEqualTo("/a");
        assertThat(uriRef22.getQuery()).isNull();
        assertThat(uriRef22.getFragment()).isNull();

        var uriRef23 = new URIReferenceParser().parse("http:a", UTF_8);
        assertThat(uriRef23.isRelativeReference()).isFalse();
        assertThat(uriRef23.getScheme()).isEqualTo("http");
        assertThat(uriRef23.hasAuthority()).isFalse();
        assertThat(uriRef23.getAuthority()).isNull();
        assertThat(uriRef23.getUserinfo()).isNull();
        assertThat(uriRef23.getHost()).isNull();
        assertThat(uriRef23.getPort()).isEqualTo(-1);
        assertThat(uriRef23.getPath()).isEqualTo("a");
        assertThat(uriRef23.getQuery()).isNull();
        assertThat(uriRef23.getFragment()).isNull();

        var uriRef24 = new URIReferenceParser().parse("//", UTF_8);
        assertThat(uriRef24.isRelativeReference()).isTrue();
        assertThat(uriRef24.getScheme()).isNull();
        assertThat(uriRef24.hasAuthority()).isTrue();
        assertThat(uriRef24.getAuthority().toString()).isEqualTo("");
        assertThat(uriRef24.getUserinfo()).isNull();
        assertThat(uriRef24.getHost().toString()).isEqualTo("");
        assertThat(uriRef24.getHost().getValue()).isEqualTo("");
        assertThat(uriRef24.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef24.getPort()).isEqualTo(-1);
        assertThat(uriRef24.getPath()).isEqualTo("");
        assertThat(uriRef24.getQuery()).isNull();
        assertThat(uriRef24.getFragment()).isNull();

        var uriRef25 = new URIReferenceParser().parse("http://example.com?q", UTF_8);
        assertThat(uriRef25.isRelativeReference()).isFalse();
        assertThat(uriRef25.getScheme()).isEqualTo("http");
        assertThat(uriRef25.hasAuthority()).isTrue();
        assertThat(uriRef25.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef25.getUserinfo()).isNull();
        assertThat(uriRef25.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef25.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef25.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef25.getPort()).isEqualTo(-1);
        assertThat(uriRef25.getPath()).isEqualTo("");
        assertThat(uriRef25.getQuery()).isEqualTo("q");
        assertThat(uriRef25.getFragment()).isNull();

        var uriRef26 = new URIReferenceParser().parse("http://example.com?", UTF_8);
        assertThat(uriRef26.isRelativeReference()).isFalse();
        assertThat(uriRef26.getScheme()).isEqualTo("http");
        assertThat(uriRef26.hasAuthority()).isTrue();
        assertThat(uriRef26.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef26.getUserinfo()).isNull();
        assertThat(uriRef26.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef26.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef26.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef26.getPort()).isEqualTo(-1);
        assertThat(uriRef26.getPath()).isEqualTo("");
        assertThat(uriRef26.getQuery()).isEqualTo("");
        assertThat(uriRef26.getFragment()).isNull();

        var uriRef27 = new URIReferenceParser().parse("http://example.com#f", UTF_8);
        assertThat(uriRef27.isRelativeReference()).isFalse();
        assertThat(uriRef27.getScheme()).isEqualTo("http");
        assertThat(uriRef27.hasAuthority()).isTrue();
        assertThat(uriRef27.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef27.getUserinfo()).isNull();
        assertThat(uriRef27.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef27.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef27.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef27.getPort()).isEqualTo(-1);
        assertThat(uriRef27.getPath()).isEqualTo("");
        assertThat(uriRef27.getQuery()).isNull();
        assertThat(uriRef27.getFragment()).isEqualTo("f");

        var uriRef28 = new URIReferenceParser().parse("http://example.com#", UTF_8);
        assertThat(uriRef28.isRelativeReference()).isFalse();
        assertThat(uriRef28.getScheme()).isEqualTo("http");
        assertThat(uriRef28.hasAuthority()).isTrue();
        assertThat(uriRef28.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef28.getUserinfo()).isNull();
        assertThat(uriRef28.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef28.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef28.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef28.getPort()).isEqualTo(-1);
        assertThat(uriRef28.getPath()).isEqualTo("");
        assertThat(uriRef28.getQuery()).isNull();
        assertThat(uriRef28.getFragment()).isEqualTo("");

        var uriRef29 = new URIReferenceParser().parse("", UTF_8);
        assertThat(uriRef29.isRelativeReference()).isTrue();
        assertThat(uriRef29.getScheme()).isNull();
        assertThat(uriRef29.hasAuthority()).isFalse();
        assertThat(uriRef29.getAuthority()).isNull();
        assertThat(uriRef29.getUserinfo()).isNull();
        assertThat(uriRef29.getHost()).isNull();
        assertThat(uriRef29.getPort()).isEqualTo(-1);
        assertThat(uriRef29.getPath()).isEqualTo("");
        assertThat(uriRef29.getQuery()).isNull();
        assertThat(uriRef29.getFragment()).isNull();

        assertThrowsIAE(
            "The path segment value \"1invalid:\" has an invalid character \":\" at the index 8.",
            () -> new URIReferenceParser().parse("1invalid://example.com", UTF_8));

        assertThrowsIAE(
            "The host value \"v@w\" has an invalid character \"@\" at the index 1.",
            () -> new URIReferenceParser().parse("http://u@v@w", UTF_8));

        assertThrowsIAE(
            "The port value \"1:2:3\" has an invalid character \":\" at the index 1.",
            () -> new URIReferenceParser().parse("http://example.com:1:2:3", UTF_8));

        assertThrowsIAE(
            "The query value \"[invalid_query]\" has an invalid character \"[\" at the index 0.",
            () -> new URIReferenceParser().parse("http://example.com?[invalid_query]", UTF_8));

        assertThrowsIAE(
            "The fragment value \"[invalid_fragment]\" has an invalid character \"[\" at the index 0.",
            () -> new URIReferenceParser().parse("http://example.com#[invalid_fragment]", UTF_8));

        assertThrowsIAE(
            "The port value \"b\" has an invalid character \"b\" at the index 0.",
            () -> new URIReferenceParser().parse("//a:b", UTF_8));

        assertThrowsIAE(
            "The port value \":\" has an invalid character \":\" at the index 0.",
            () -> new URIReferenceParser().parse("//::", UTF_8));
    }
}
