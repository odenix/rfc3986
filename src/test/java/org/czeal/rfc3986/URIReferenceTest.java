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
import static org.czeal.rfc3986.TestUtils.assertThrowsISE;
import static org.czeal.rfc3986.TestUtils.assertThrowsNPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.czeal.rfc3986.HostType.IPV4;
import static org.czeal.rfc3986.HostType.IPV6;
import static org.czeal.rfc3986.HostType.IPVFUTURE;
import static org.czeal.rfc3986.HostType.REGNAME;
import org.junit.jupiter.api.Test;


class URIReferenceTest
{
    @Test
    void parse()
    {
        var uriRef1 = URIReference.parse("http://example.com");
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

        var uriRef2 = URIReference.parse("hTTp://example.com");
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

        var uriRef3 = URIReference.parse("//example.com");
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

        var uriRef4 = URIReference.parse("http:");
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

        var uriRef5 = URIReference.parse("http://john@example.com");
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

        var uriRef6 = URIReference.parse("http://%6A%6F%68%6E@example.com");
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

        var uriRef7 = URIReference.parse("http://101.102.103.104");
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

        var uriRef8 = URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
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

        var uriRef9 = URIReference.parse("http://[2001:db8:0:1:1:1:1:1]");
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

        var uriRef10 = URIReference.parse("http://[2001:0:9d38:6abd:0:0:0:42]");
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

        var uriRef11 = URIReference.parse("http://[fe80::1]");
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

        var uriRef12 = URIReference.parse("http://[2001:0:3238:DFE1:63::FEFB]");
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

        var uriRef13 = URIReference.parse("http://[v1.fe80::a+en1]");
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

        var uriRef14 = URIReference.parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D");
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

        var uriRef15 = URIReference.parse("http://");
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

        var uriRef16 = URIReference.parse("http:///a");
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

        var uriRef17 = URIReference.parse("http://example.com:80");
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

        var uriRef18 = URIReference.parse("http://example.com:");
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

        var uriRef19 = URIReference.parse("http://example.com:001");
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

        var uriRef20 = URIReference.parse("http://example.com/a/b/c");
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

        var uriRef21 = URIReference.parse("http://example.com/%61/%62/%63");
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

        var uriRef22 = URIReference.parse("http:/a");
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

        var uriRef23 = URIReference.parse("http:a");
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

        var uriRef24 = URIReference.parse("//");
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

        var uriRef25 = URIReference.parse("http://example.com?q");
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

        var uriRef26 = URIReference.parse("http://example.com?");
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

        var uriRef27 = URIReference.parse("http://example.com#f");
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

        var uriRef28 = URIReference.parse("http://example.com#");
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

        var uriRef29 = URIReference.parse("");
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
            () -> URIReference.parse("1invalid://example.com"));

        assertThrowsIAE(
            "The host value \"v@w\" has an invalid character \"@\" at the index 1.",
            () -> URIReference.parse("http://u@v@w"));

        assertThrowsIAE(
            "The port value \"1:2:3\" has an invalid character \":\" at the index 1.",
            () -> URIReference.parse("http://example.com:1:2:3"));

        assertThrowsIAE(
            "The query value \"[invalid_query]\" has an invalid character \"[\" at the index 0.",
            () -> URIReference.parse("http://example.com?[invalid_query]"));

        assertThrowsIAE(
            "The fragment value \"[invalid_fragment]\" has an invalid character \"[\" at the index 0.",
            () -> URIReference.parse("http://example.com#[invalid_fragment]"));

        assertThrowsIAE(
            "The port value \"b\" has an invalid character \"b\" at the index 0.",
            () -> URIReference.parse("//a:b"));

        assertThrowsIAE(
            "The port value \":\" has an invalid character \":\" at the index 0.",
            () -> URIReference.parse("//::"));
    }


    @Test
    void is_relative_reference()
    {
        assertThat(URIReference.parse("http://example.com").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("hTTp://example.com").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("//example.com").isRelativeReference()).isTrue();
        assertThat(URIReference.parse("http:").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://john@example.com").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://%6A%6F%68%6E@example.com").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://101.102.103.104").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://[2001:db8:0:1:1:1:1:1]").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://[2001:0:9d38:6abd:0:0:0:42]").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://[fe80::1]").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://[2001:0:3238:DFE1:63::FEFB]").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://[v1.fe80::a+en1]").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http:///a").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://example.com:80").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://example.com:").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://example.com:001").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://example.com/a/b/c").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://example.com/%61/%62/%63").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://example.com?q").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://example.com?").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://example.com#f").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http://example.com#").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http:/a").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("http:a").isRelativeReference()).isFalse();
        assertThat(URIReference.parse("//").isRelativeReference()).isTrue();
        assertThat(URIReference.parse("").isRelativeReference()).isTrue();
    }


    @Test
    void get_scheme()
    {
        assertThat(URIReference.parse("http://example.com").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("hTTp://example.com").getScheme()).isEqualTo("hTTp");
        assertThat(URIReference.parse("//example.com").getScheme()).isNull();
        assertThat(URIReference.parse("http:").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://john@example.com").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://%6A%6F%68%6E@example.com").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://101.102.103.104").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://[2001:db8:0:1:1:1:1:1]").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://[2001:0:9d38:6abd:0:0:0:42]").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://[fe80::1]").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://[2001:0:3238:DFE1:63::FEFB]").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://[v1.fe80::a+en1]").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http:///a").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://example.com:80").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://example.com:").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://example.com:001").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://example.com/a/b/c").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://example.com/%61/%62/%63").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://example.com?q").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://example.com?").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://example.com#f").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http://example.com#").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http:/a").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("http:a").getScheme()).isEqualTo("http");
        assertThat(URIReference.parse("//").getScheme()).isNull();
        assertThat(URIReference.parse("").getScheme()).isNull();
    }


    @Test
    void has_authority()
    {
        assertThat(URIReference.parse("http://example.com").hasAuthority()).isTrue();
        assertThat(URIReference.parse("hTTp://example.com").hasAuthority()).isTrue();
        assertThat(URIReference.parse("//example.com").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http:").hasAuthority()).isFalse();
        assertThat(URIReference.parse("http://john@example.com").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://%6A%6F%68%6E@example.com").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://101.102.103.104").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://[2001:db8:0:1:1:1:1:1]").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://[2001:0:9d38:6abd:0:0:0:42]").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://[fe80::1]").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://[2001:0:3238:DFE1:63::FEFB]").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://[v1.fe80::a+en1]").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http:///a").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://example.com:80").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://example.com:").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://example.com:001").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://example.com/a/b/c").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://example.com/%61/%62/%63").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://example.com?q").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://example.com?").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://example.com#f").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http://example.com#").hasAuthority()).isTrue();
        assertThat(URIReference.parse("http:/a").hasAuthority()).isFalse();
        assertThat(URIReference.parse("http:a").hasAuthority()).isFalse();
        assertThat(URIReference.parse("//").hasAuthority()).isTrue();
        assertThat(URIReference.parse("").hasAuthority()).isFalse();
    }


    @Test
    void get_authority()
    {
        assertThat(URIReference.parse("http://example.com").getAuthority()).isEqualTo(Authority.parse("example.com"));
        assertThat(URIReference.parse("hTTp://example.com").getAuthority()).isEqualTo(Authority.parse("example.com"));
        assertThat(URIReference.parse("//example.com").getAuthority()).isEqualTo(Authority.parse("example.com"));
        assertThat(URIReference.parse("http:").getAuthority()).isNull();
        assertThat(URIReference.parse("http://john@example.com").getAuthority()).isEqualTo(Authority.parse("john@example.com"));
        assertThat(URIReference.parse("http://%6A%6F%68%6E@example.com").getAuthority()).isEqualTo(Authority.parse("%6A%6F%68%6E@example.com"));
        assertThat(URIReference.parse("http://101.102.103.104").getAuthority()).isEqualTo(Authority.parse("101.102.103.104"));
        assertThat(URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").getAuthority()).isEqualTo(Authority.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]"));
        assertThat(URIReference.parse("http://[2001:db8:0:1:1:1:1:1]").getAuthority()).isEqualTo(Authority.parse("[2001:db8:0:1:1:1:1:1]"));
        assertThat(URIReference.parse("http://[2001:0:9d38:6abd:0:0:0:42]").getAuthority()).isEqualTo(Authority.parse("[2001:0:9d38:6abd:0:0:0:42]"));
        assertThat(URIReference.parse("http://[fe80::1]").getAuthority()).isEqualTo(Authority.parse("[fe80::1]"));
        assertThat(URIReference.parse("http://[2001:0:3238:DFE1:63::FEFB]").getAuthority()).isEqualTo(Authority.parse("[2001:0:3238:DFE1:63::FEFB]"));
        assertThat(URIReference.parse("http://[v1.fe80::a+en1]").getAuthority()).isEqualTo(Authority.parse("[v1.fe80::a+en1]"));
        assertThat(URIReference.parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D").getAuthority()).isEqualTo(Authority.parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D"));
        assertThat(URIReference.parse("http://").getAuthority()).isEqualTo(Authority.parse(""));
        assertThat(URIReference.parse("http:///a").getAuthority()).isEqualTo(Authority.parse(""));
        assertThat(URIReference.parse("http://example.com:80").getAuthority()).isEqualTo(Authority.parse("example.com:80"));
        assertThat(URIReference.parse("http://example.com:").getAuthority()).isEqualTo(Authority.parse("example.com"));
        assertThat(URIReference.parse("http://example.com:001").getAuthority()).isEqualTo(Authority.parse("example.com:001"));
        assertThat(URIReference.parse("http://example.com/a/b/c").getAuthority()).isEqualTo(Authority.parse("example.com"));
        assertThat(URIReference.parse("http://example.com/%61/%62/%63").getAuthority()).isEqualTo(Authority.parse("example.com"));
        assertThat(URIReference.parse("http://example.com?q").getAuthority()).isEqualTo(Authority.parse("example.com"));
        assertThat(URIReference.parse("http://example.com?").getAuthority()).isEqualTo(Authority.parse("example.com"));
        assertThat(URIReference.parse("http://example.com#f").getAuthority()).isEqualTo(Authority.parse("example.com"));
        assertThat(URIReference.parse("http://example.com#").getAuthority()).isEqualTo(Authority.parse("example.com"));
        assertThat(URIReference.parse("http:/a").getAuthority()).isNull();
        assertThat(URIReference.parse("http:a").getAuthority()).isNull();
        assertThat(URIReference.parse("//").getAuthority()).isEqualTo(Authority.parse(""));
        assertThat(URIReference.parse("").getAuthority()).isNull();
    }


    @Test
    void get_user_info()
    {
        assertThat(URIReference.parse("http://example.com").getUserinfo()).isNull();
        assertThat(URIReference.parse("hTTp://example.com").getUserinfo()).isNull();
        assertThat(URIReference.parse("//example.com").getUserinfo()).isNull();
        assertThat(URIReference.parse("http:").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://john@example.com").getUserinfo()).isEqualTo("john");
        assertThat(URIReference.parse("http://%6A%6F%68%6E@example.com").getUserinfo()).isEqualTo("%6A%6F%68%6E");
        assertThat(URIReference.parse("http://101.102.103.104").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://[2001:db8:0:1:1:1:1:1]").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://[2001:0:9d38:6abd:0:0:0:42]").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://[fe80::1]").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://[2001:0:3238:DFE1:63::FEFB]").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://[v1.fe80::a+en1]").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://").getUserinfo()).isNull();
        assertThat(URIReference.parse("http:///a").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://example.com:80").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://example.com:").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://example.com:001").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://example.com/a/b/c").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://example.com/%61/%62/%63").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://example.com?q").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://example.com?").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://example.com#f").getUserinfo()).isNull();
        assertThat(URIReference.parse("http://example.com#").getUserinfo()).isNull();
        assertThat(URIReference.parse("http:/a").getUserinfo()).isNull();
        assertThat(URIReference.parse("http:a").getUserinfo()).isNull();
        assertThat(URIReference.parse("//").getUserinfo()).isNull();
        assertThat(URIReference.parse("").getUserinfo()).isNull();
    }


    @Test
    void host()
    {
        assertThat(URIReference.parse("http://example.com").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(URIReference.parse("hTTp://example.com").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(URIReference.parse("//example.com").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(URIReference.parse("http:").getHost()).isNull();
        assertThat(URIReference.parse("http://john@example.com").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(URIReference.parse("http://%6A%6F%68%6E@example.com").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(URIReference.parse("http://101.102.103.104").getHost()).isEqualTo(new Host(IPV4, "101.102.103.104"));
        assertThat(URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").getHost()).isEqualTo(new Host(IPV6, "[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]"));
        assertThat(URIReference.parse("http://[2001:db8:0:1:1:1:1:1]").getHost()).isEqualTo(new Host(IPV6, "[2001:db8:0:1:1:1:1:1]"));
        assertThat(URIReference.parse("http://[2001:0:9d38:6abd:0:0:0:42]").getHost()).isEqualTo(new Host(IPV6, "[2001:0:9d38:6abd:0:0:0:42]"));
        assertThat(URIReference.parse("http://[fe80::1]").getHost()).isEqualTo(new Host(IPV6, "[fe80::1]"));
        assertThat(URIReference.parse("http://[2001:0:3238:DFE1:63::FEFB]").getHost()).isEqualTo(new Host(IPV6, "[2001:0:3238:DFE1:63::FEFB]"));
        assertThat(URIReference.parse("http://[v1.fe80::a+en1]").getHost()).isEqualTo(new Host(IPVFUTURE, "[v1.fe80::a+en1]"));
        assertThat(URIReference.parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D").getHost()).isEqualTo(new Host(REGNAME, "%65%78%61%6D%70%6C%65%2E%63%6F%6D"));
        assertThat(URIReference.parse("http://").getHost()).isEqualTo(new Host(REGNAME, ""));
        assertThat(URIReference.parse("http:///a").getHost()).isEqualTo(new Host(REGNAME, ""));
        assertThat(URIReference.parse("http://example.com:80").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(URIReference.parse("http://example.com:").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(URIReference.parse("http://example.com:001").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(URIReference.parse("http://example.com/a/b/c").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(URIReference.parse("http://example.com/%61/%62/%63").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(URIReference.parse("http://example.com?q").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(URIReference.parse("http://example.com?").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(URIReference.parse("http://example.com#f").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(URIReference.parse("http://example.com#").getHost()).isEqualTo(new Host(REGNAME, "example.com"));
        assertThat(URIReference.parse("http:/a").getHost()).isNull();
        assertThat(URIReference.parse("http:a").getHost()).isNull();
        assertThat(URIReference.parse("//").getHost()).isEqualTo(new Host(REGNAME, ""));
        assertThat(URIReference.parse("").getHost()).isNull();
    }


    @Test
    void get_port()
    {
        assertThat(URIReference.parse("http://example.com").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("hTTp://example.com").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("//example.com").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http:").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://john@example.com").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://%6A%6F%68%6E@example.com").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://101.102.103.104").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://[2001:db8:0:1:1:1:1:1]").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://[2001:0:9d38:6abd:0:0:0:42]").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://[fe80::1]").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://[2001:0:3238:DFE1:63::FEFB]").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://[v1.fe80::a+en1]").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http:///a").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://example.com:80").getPort()).isEqualTo(80);
        assertThat(URIReference.parse("http://example.com:").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://example.com:001").getPort()).isEqualTo(1);
        assertThat(URIReference.parse("http://example.com/a/b/c").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://example.com/%61/%62/%63").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://example.com?q").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://example.com?").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://example.com#f").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http://example.com#").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http:/a").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("http:a").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("//").getPort()).isEqualTo(-1);
        assertThat(URIReference.parse("").getPort()).isEqualTo(-1);
    }


    @Test
    void get_path()
    {
        assertThat(URIReference.parse("http://example.com").getPath()).isEqualTo("");
        assertThat(URIReference.parse("hTTp://example.com").getPath()).isEqualTo("");
        assertThat(URIReference.parse("//example.com").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http:").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://john@example.com").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://%6A%6F%68%6E@example.com").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://101.102.103.104").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://[2001:db8:0:1:1:1:1:1]").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://[2001:0:9d38:6abd:0:0:0:42]").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://[fe80::1]").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://[2001:0:3238:DFE1:63::FEFB]").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://[v1.fe80::a+en1]").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http:///a").getPath()).isEqualTo("/a");
        assertThat(URIReference.parse("http://example.com:80").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://example.com:").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://example.com:001").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://example.com/a/b/c").getPath()).isEqualTo("/a/b/c");
        assertThat(URIReference.parse("http://example.com/%61/%62/%63").getPath()).isEqualTo("/%61/%62/%63");
        assertThat(URIReference.parse("http://example.com?q").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://example.com?").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://example.com#f").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http://example.com#").getPath()).isEqualTo("");
        assertThat(URIReference.parse("http:/a").getPath()).isEqualTo("/a");
        assertThat(URIReference.parse("http:a").getPath()).isEqualTo("a");
        assertThat(URIReference.parse("//").getPath()).isEqualTo("");
        assertThat(URIReference.parse("").getPath()).isEqualTo("");
    }


    @Test
    void get_query()
    {
        assertThat(URIReference.parse("http://example.com").getQuery()).isNull();
        assertThat(URIReference.parse("hTTp://example.com").getQuery()).isNull();
        assertThat(URIReference.parse("//example.com").getQuery()).isNull();
        assertThat(URIReference.parse("http:").getQuery()).isNull();
        assertThat(URIReference.parse("http://john@example.com").getQuery()).isNull();
        assertThat(URIReference.parse("http://%6A%6F%68%6E@example.com").getQuery()).isNull();
        assertThat(URIReference.parse("http://101.102.103.104").getQuery()).isNull();
        assertThat(URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").getQuery()).isNull();
        assertThat(URIReference.parse("http://[2001:db8:0:1:1:1:1:1]").getQuery()).isNull();
        assertThat(URIReference.parse("http://[2001:0:9d38:6abd:0:0:0:42]").getQuery()).isNull();
        assertThat(URIReference.parse("http://[fe80::1]").getQuery()).isNull();
        assertThat(URIReference.parse("http://[2001:0:3238:DFE1:63::FEFB]").getQuery()).isNull();
        assertThat(URIReference.parse("http://[v1.fe80::a+en1]").getQuery()).isNull();
        assertThat(URIReference.parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D").getQuery()).isNull();
        assertThat(URIReference.parse("http://").getQuery()).isNull();
        assertThat(URIReference.parse("http:///a").getQuery()).isNull();
        assertThat(URIReference.parse("http://example.com:80").getQuery()).isNull();
        assertThat(URIReference.parse("http://example.com:").getQuery()).isNull();
        assertThat(URIReference.parse("http://example.com:001").getQuery()).isNull();
        assertThat(URIReference.parse("http://example.com/a/b/c").getQuery()).isNull();
        assertThat(URIReference.parse("http://example.com/%61/%62/%63").getQuery()).isNull();
        assertThat(URIReference.parse("http://example.com?q").getQuery()).isEqualTo("q");
        assertThat(URIReference.parse("http://example.com?").getQuery()).isEqualTo("");
        assertThat(URIReference.parse("http://example.com#f").getQuery()).isNull();
        assertThat(URIReference.parse("http://example.com#").getQuery()).isNull();
        assertThat(URIReference.parse("http:/a").getQuery()).isNull();
        assertThat(URIReference.parse("http:a").getQuery()).isNull();
        assertThat(URIReference.parse("//").getQuery()).isNull();
        assertThat(URIReference.parse("").getQuery()).isNull();
    }


    @Test
    void get_fragment()
    {
        assertThat(URIReference.parse("http://example.com").getFragment()).isNull();
        assertThat(URIReference.parse("hTTp://example.com").getFragment()).isNull();
        assertThat(URIReference.parse("//example.com").getFragment()).isNull();
        assertThat(URIReference.parse("http:").getFragment()).isNull();
        assertThat(URIReference.parse("http://john@example.com").getFragment()).isNull();
        assertThat(URIReference.parse("http://%6A%6F%68%6E@example.com").getFragment()).isNull();
        assertThat(URIReference.parse("http://101.102.103.104").getFragment()).isNull();
        assertThat(URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").getFragment()).isNull();
        assertThat(URIReference.parse("http://[2001:db8:0:1:1:1:1:1]").getFragment()).isNull();
        assertThat(URIReference.parse("http://[2001:0:9d38:6abd:0:0:0:42]").getFragment()).isNull();
        assertThat(URIReference.parse("http://[fe80::1]").getFragment()).isNull();
        assertThat(URIReference.parse("http://[2001:0:3238:DFE1:63::FEFB]").getFragment()).isNull();
        assertThat(URIReference.parse("http://[v1.fe80::a+en1]").getFragment()).isNull();
        assertThat(URIReference.parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D").getFragment()).isNull();
        assertThat(URIReference.parse("http://").getFragment()).isNull();
        assertThat(URIReference.parse("http:///a").getFragment()).isNull();
        assertThat(URIReference.parse("http://example.com:80").getFragment()).isNull();
        assertThat(URIReference.parse("http://example.com:").getFragment()).isNull();
        assertThat(URIReference.parse("http://example.com:001").getFragment()).isNull();
        assertThat(URIReference.parse("http://example.com/a/b/c").getFragment()).isNull();
        assertThat(URIReference.parse("http://example.com/%61/%62/%63").getFragment()).isNull();
        assertThat(URIReference.parse("http://example.com?q").getFragment()).isNull();
        assertThat(URIReference.parse("http://example.com?").getFragment()).isNull();
        assertThat(URIReference.parse("http://example.com#f").getFragment()).isEqualTo("f");
        assertThat(URIReference.parse("http://example.com#").getFragment()).isEqualTo("");
        assertThat(URIReference.parse("http:/a").getFragment()).isNull();
        assertThat(URIReference.parse("http:a").getFragment()).isNull();
        assertThat(URIReference.parse("//").getFragment()).isNull();
        assertThat(URIReference.parse("").getFragment()).isNull();
    }


    @Test
    void equals()
    {
        assertThat(URIReference.parse("http://example.com")).isEqualTo(URIReference.parse("http://example.com"));
        assertThat(URIReference.parse("hTTp://example.com")).isEqualTo(URIReference.parse("hTTp://example.com"));
        assertThat(URIReference.parse("//example.com")).isEqualTo(URIReference.parse("//example.com"));
        assertThat(URIReference.parse("http:")).isEqualTo(URIReference.parse("http:"));
        assertThat(URIReference.parse("http://john@example.com")).isEqualTo(URIReference.parse("http://john@example.com"));
        assertThat(URIReference.parse("http://%6A%6F%68%6E@example.com")).isEqualTo(URIReference.parse("http://%6A%6F%68%6E@example.com"));
        assertThat(URIReference.parse("http://101.102.103.104")).isEqualTo(URIReference.parse("http://101.102.103.104"));
        assertThat(URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]")).isEqualTo(URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]"));
        assertThat(URIReference.parse("http://[2001:db8:0:1:1:1:1:1]")).isEqualTo(URIReference.parse("http://[2001:db8:0:1:1:1:1:1]"));
        assertThat(URIReference.parse("http://[2001:0:9d38:6abd:0:0:0:42]")).isEqualTo(URIReference.parse("http://[2001:0:9d38:6abd:0:0:0:42]"));
        assertThat(URIReference.parse("http://[fe80::1]")).isEqualTo(URIReference.parse("http://[fe80::1]"));
        assertThat(URIReference.parse("http://[2001:0:3238:DFE1:63::FEFB]")).isEqualTo(URIReference.parse("http://[2001:0:3238:DFE1:63::FEFB]"));
        assertThat(URIReference.parse("http://[v1.fe80::a+en1]")).isEqualTo(URIReference.parse("http://[v1.fe80::a+en1]"));
        assertThat(URIReference.parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D")).isEqualTo(URIReference.parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D"));
        assertThat(URIReference.parse("http://")).isEqualTo(URIReference.parse("http://"));
        assertThat(URIReference.parse("http:///a")).isEqualTo(URIReference.parse("http:///a"));
        assertThat(URIReference.parse("http://example.com:80")).isEqualTo(URIReference.parse("http://example.com:80"));
        assertThat(URIReference.parse("http://example.com:")).isEqualTo(URIReference.parse("http://example.com:"));
        assertThat(URIReference.parse("http://example.com:001")).isEqualTo(URIReference.parse("http://example.com:001"));
        assertThat(URIReference.parse("http://example.com/a/b/c")).isEqualTo(URIReference.parse("http://example.com/a/b/c"));
        assertThat(URIReference.parse("http://example.com/%61/%62/%63")).isEqualTo(URIReference.parse("http://example.com/%61/%62/%63"));
        assertThat(URIReference.parse("http://example.com?q")).isEqualTo(URIReference.parse("http://example.com?q"));
        assertThat(URIReference.parse("http://example.com?")).isEqualTo(URIReference.parse("http://example.com?"));
        assertThat(URIReference.parse("http://example.com#f")).isEqualTo(URIReference.parse("http://example.com#f"));
        assertThat(URIReference.parse("http://example.com#")).isEqualTo(URIReference.parse("http://example.com#"));
        assertThat(URIReference.parse("http:/a")).isEqualTo(URIReference.parse("http:/a"));
        assertThat(URIReference.parse("http:a")).isEqualTo(URIReference.parse("http:a"));
        assertThat(URIReference.parse("//")).isEqualTo(URIReference.parse("//"));
        assertThat(URIReference.parse("")).isEqualTo(URIReference.parse(""));
    }


    @Test
    void resolve()
    {
        var uriRef1 = URIReference.parse("http://a/b/c/d;p?q").resolve("g:h");
        assertThat(uriRef1.toString()).isEqualTo("g:h");
        assertThat(uriRef1.isRelativeReference()).isFalse();
        assertThat(uriRef1.getScheme()).isEqualTo("g");
        assertThat(uriRef1.hasAuthority()).isFalse();
        assertThat(uriRef1.getAuthority()).isNull();
        assertThat(uriRef1.getUserinfo()).isNull();
        assertThat(uriRef1.getHost()).isNull();
        assertThat(uriRef1.getPort()).isEqualTo(-1);
        assertThat(uriRef1.getPath()).isEqualTo("h");
        assertThat(uriRef1.getQuery()).isNull();
        assertThat(uriRef1.getFragment()).isNull();

        var uriRef2 = URIReference.parse("http://a/b/c/d;p?q").resolve("g");
        assertThat(uriRef2.toString()).isEqualTo("http://a/b/c/g");
        assertThat(uriRef2.isRelativeReference()).isFalse();
        assertThat(uriRef2.getScheme()).isEqualTo("http");
        assertThat(uriRef2.hasAuthority()).isTrue();
        assertThat(uriRef2.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef2.getUserinfo()).isNull();
        assertThat(uriRef2.getHost().toString()).isEqualTo("a");
        assertThat(uriRef2.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef2.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef2.getPort()).isEqualTo(-1);
        assertThat(uriRef2.getPath()).isEqualTo("/b/c/g");
        assertThat(uriRef2.getQuery()).isNull();
        assertThat(uriRef2.getFragment()).isNull();

        var uriRef3 = URIReference.parse("http://a/b/c/d;p?q").resolve("./g");
        assertThat(uriRef3.toString()).isEqualTo("http://a/b/c/g");
        assertThat(uriRef3.isRelativeReference()).isFalse();
        assertThat(uriRef3.getScheme()).isEqualTo("http");
        assertThat(uriRef3.hasAuthority()).isTrue();
        assertThat(uriRef3.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef3.getUserinfo()).isNull();
        assertThat(uriRef3.getHost().toString()).isEqualTo("a");
        assertThat(uriRef3.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef3.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef3.getPort()).isEqualTo(-1);
        assertThat(uriRef3.getPath()).isEqualTo("/b/c/g");
        assertThat(uriRef3.getQuery()).isNull();
        assertThat(uriRef3.getFragment()).isNull();

        var uriRef4 = URIReference.parse("http://a/b/c/d;p?q").resolve("g/");
        assertThat(uriRef4.toString()).isEqualTo("http://a/b/c/g/");
        assertThat(uriRef4.isRelativeReference()).isFalse();
        assertThat(uriRef4.getScheme()).isEqualTo("http");
        assertThat(uriRef4.hasAuthority()).isTrue();
        assertThat(uriRef4.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef4.getUserinfo()).isNull();
        assertThat(uriRef4.getHost().toString()).isEqualTo("a");
        assertThat(uriRef4.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef4.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef4.getPort()).isEqualTo(-1);
        assertThat(uriRef4.getPath()).isEqualTo("/b/c/g/");
        assertThat(uriRef4.getQuery()).isNull();
        assertThat(uriRef4.getFragment()).isNull();

        var uriRef5 = URIReference.parse("http://a/b/c/d;p?q").resolve("/g");
        assertThat(uriRef5.toString()).isEqualTo("http://a/g");
        assertThat(uriRef5.isRelativeReference()).isFalse();
        assertThat(uriRef5.getScheme()).isEqualTo("http");
        assertThat(uriRef4.hasAuthority()).isTrue();
        assertThat(uriRef4.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef4.getUserinfo()).isNull();
        assertThat(uriRef4.getHost().toString()).isEqualTo("a");
        assertThat(uriRef4.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef4.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef4.getPort()).isEqualTo(-1);
        assertThat(uriRef5.getPath()).isEqualTo("/g");
        assertThat(uriRef5.getQuery()).isNull();
        assertThat(uriRef5.getFragment()).isNull();

        var uriRef6 = URIReference.parse("http://a/b/c/d;p?q").resolve("//g");
        assertThat(uriRef6.toString()).isEqualTo("http://g");
        assertThat(uriRef6.isRelativeReference()).isFalse();
        assertThat(uriRef6.getScheme()).isEqualTo("http");
        assertThat(uriRef6.hasAuthority()).isTrue();
        assertThat(uriRef6.getAuthority().toString()).isEqualTo("g");
        assertThat(uriRef6.getUserinfo()).isNull();
        assertThat(uriRef6.getHost().toString()).isEqualTo("g");
        assertThat(uriRef6.getHost().getValue()).isEqualTo("g");
        assertThat(uriRef6.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef6.getPort()).isEqualTo(-1);
        assertThat(uriRef6.getPath()).isEqualTo("");
        assertThat(uriRef6.getQuery()).isNull();
        assertThat(uriRef6.getFragment()).isNull();

        var uriRef7 = URIReference.parse("http://a/b/c/d;p?q").resolve("?y");
        assertThat(uriRef7.toString()).isEqualTo("http://a/b/c/d;p?y");
        assertThat(uriRef7.isRelativeReference()).isFalse();
        assertThat(uriRef7.getScheme()).isEqualTo("http");
        assertThat(uriRef7.hasAuthority()).isTrue();
        assertThat(uriRef7.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef7.getUserinfo()).isNull();
        assertThat(uriRef7.getHost().toString()).isEqualTo("a");
        assertThat(uriRef7.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef7.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef7.getPort()).isEqualTo(-1);
        assertThat(uriRef7.getPath()).isEqualTo("/b/c/d;p");
        assertThat(uriRef7.getQuery()).isEqualTo("y");
        assertThat(uriRef7.getFragment()).isNull();

        var uriRef8 = URIReference.parse("http://a/b/c/d;p?q").resolve("g?y");
        assertThat(uriRef8.toString()).isEqualTo("http://a/b/c/g?y");
        assertThat(uriRef8.isRelativeReference()).isFalse();
        assertThat(uriRef8.getScheme()).isEqualTo("http");
        assertThat(uriRef8.hasAuthority()).isTrue();
        assertThat(uriRef8.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef8.getUserinfo()).isNull();
        assertThat(uriRef8.getHost().toString()).isEqualTo("a");
        assertThat(uriRef8.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef8.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef8.getPort()).isEqualTo(-1);
        assertThat(uriRef8.getPath()).isEqualTo("/b/c/g");
        assertThat(uriRef8.getQuery()).isEqualTo("y");
        assertThat(uriRef8.getFragment()).isNull();

        var uriRef9 = URIReference.parse("http://a/b/c/d;p?q").resolve("#s");
        assertThat(uriRef9.toString()).isEqualTo("http://a/b/c/d;p?q#s");
        assertThat(uriRef9.isRelativeReference()).isFalse();
        assertThat(uriRef9.getScheme()).isEqualTo("http");
        assertThat(uriRef9.hasAuthority()).isTrue();
        assertThat(uriRef9.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef9.getUserinfo()).isNull();
        assertThat(uriRef9.getHost().toString()).isEqualTo("a");
        assertThat(uriRef9.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef9.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef9.getPort()).isEqualTo(-1);
        assertThat(uriRef9.getPath()).isEqualTo("/b/c/d;p");
        assertThat(uriRef9.getQuery()).isEqualTo("q");
        assertThat(uriRef9.getFragment()).isEqualTo("s");

        var uriRef10 = URIReference.parse("http://a/b/c/d;p?q").resolve("g#s");
        assertThat(uriRef10.toString()).isEqualTo("http://a/b/c/g#s");
        assertThat(uriRef10.isRelativeReference()).isFalse();
        assertThat(uriRef10.getScheme()).isEqualTo("http");
        assertThat(uriRef10.hasAuthority()).isTrue();
        assertThat(uriRef10.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef10.getUserinfo()).isNull();
        assertThat(uriRef10.getHost().toString()).isEqualTo("a");
        assertThat(uriRef10.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef10.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef10.getPort()).isEqualTo(-1);
        assertThat(uriRef10.getPath()).isEqualTo("/b/c/g");
        assertThat(uriRef10.getQuery()).isNull();
        assertThat(uriRef10.getFragment()).isEqualTo("s");

        var uriRef11 = URIReference.parse("http://a/b/c/d;p?q").resolve("g?y#s");
        assertThat(uriRef11.toString()).isEqualTo("http://a/b/c/g?y#s");
        assertThat(uriRef11.isRelativeReference()).isFalse();
        assertThat(uriRef11.getScheme()).isEqualTo("http");
        assertThat(uriRef11.hasAuthority()).isTrue();
        assertThat(uriRef11.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef11.getUserinfo()).isNull();
        assertThat(uriRef11.getHost().toString()).isEqualTo("a");
        assertThat(uriRef11.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef11.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef11.getPort()).isEqualTo(-1);
        assertThat(uriRef11.getPath()).isEqualTo("/b/c/g");
        assertThat(uriRef11.getQuery()).isEqualTo("y");
        assertThat(uriRef11.getFragment()).isEqualTo("s");

        var uriRef12 = URIReference.parse("http://a/b/c/d;p?q").resolve(";x");
        assertThat(uriRef12.toString()).isEqualTo("http://a/b/c/;x");
        assertThat(uriRef12.isRelativeReference()).isFalse();
        assertThat(uriRef12.getScheme()).isEqualTo("http");
        assertThat(uriRef12.hasAuthority()).isTrue();
        assertThat(uriRef12.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef12.getUserinfo()).isNull();
        assertThat(uriRef12.getHost().toString()).isEqualTo("a");
        assertThat(uriRef12.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef12.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef12.getPort()).isEqualTo(-1);
        assertThat(uriRef12.getPath()).isEqualTo("/b/c/;x");
        assertThat(uriRef12.getQuery()).isNull();
        assertThat(uriRef12.getFragment()).isNull();

        var uriRef13 = URIReference.parse("http://a/b/c/d;p?q").resolve("g;x");
        assertThat(uriRef13.toString()).isEqualTo("http://a/b/c/g;x");
        assertThat(uriRef13.isRelativeReference()).isFalse();
        assertThat(uriRef13.getScheme()).isEqualTo("http");
        assertThat(uriRef13.hasAuthority()).isTrue();
        assertThat(uriRef13.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef13.getUserinfo()).isNull();
        assertThat(uriRef13.getHost().toString()).isEqualTo("a");
        assertThat(uriRef13.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef13.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef13.getPort()).isEqualTo(-1);
        assertThat(uriRef13.getPath()).isEqualTo("/b/c/g;x");
        assertThat(uriRef13.getQuery()).isNull();
        assertThat(uriRef13.getFragment()).isNull();

        var uriRef14 = URIReference.parse("http://a/b/c/d;p?q").resolve("g;x?y#s");
        assertThat(uriRef14.toString()).isEqualTo("http://a/b/c/g;x?y#s");
        assertThat(uriRef14.isRelativeReference()).isFalse();
        assertThat(uriRef14.getScheme()).isEqualTo("http");
        assertThat(uriRef14.hasAuthority()).isTrue();
        assertThat(uriRef14.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef14.getUserinfo()).isNull();
        assertThat(uriRef14.getHost().toString()).isEqualTo("a");
        assertThat(uriRef14.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef14.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef14.getPort()).isEqualTo(-1);
        assertThat(uriRef14.getPath()).isEqualTo("/b/c/g;x");
        assertThat(uriRef14.getQuery()).isEqualTo("y");
        assertThat(uriRef14.getFragment()).isEqualTo("s");

        var uriRef15 = URIReference.parse("http://a/b/c/d;p?q").resolve("");
        assertThat(uriRef15.toString()).isEqualTo("http://a/b/c/d;p?q");
        assertThat(uriRef15.isRelativeReference()).isFalse();
        assertThat(uriRef15.getScheme()).isEqualTo("http");
        assertThat(uriRef15.hasAuthority()).isTrue();
        assertThat(uriRef15.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef15.getUserinfo()).isNull();
        assertThat(uriRef15.getHost().toString()).isEqualTo("a");
        assertThat(uriRef15.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef15.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef15.getPort()).isEqualTo(-1);
        assertThat(uriRef15.getPath()).isEqualTo("/b/c/d;p");
        assertThat(uriRef15.getQuery()).isEqualTo("q");
        assertThat(uriRef15.getFragment()).isNull();

        var uriRef16 = URIReference.parse("http://a/b/c/d;p?q").resolve(".");
        assertThat(uriRef16.toString()).isEqualTo("http://a/b/c/");
        assertThat(uriRef16.isRelativeReference()).isFalse();
        assertThat(uriRef16.getScheme()).isEqualTo("http");
        assertThat(uriRef16.hasAuthority()).isTrue();
        assertThat(uriRef16.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef16.getUserinfo()).isNull();
        assertThat(uriRef16.getHost().toString()).isEqualTo("a");
        assertThat(uriRef16.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef16.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef16.getPort()).isEqualTo(-1);
        assertThat(uriRef16.getPath()).isEqualTo("/b/c/");
        assertThat(uriRef16.getQuery()).isNull();
        assertThat(uriRef16.getFragment()).isNull();

        var uriRef17 = URIReference.parse("http://a/b/c/d;p?q").resolve("./");
        assertThat(uriRef17.toString()).isEqualTo("http://a/b/c/");
        assertThat(uriRef17.isRelativeReference()).isFalse();
        assertThat(uriRef17.getScheme()).isEqualTo("http");
        assertThat(uriRef17.hasAuthority()).isTrue();
        assertThat(uriRef17.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef17.getUserinfo()).isNull();
        assertThat(uriRef17.getHost().toString()).isEqualTo("a");
        assertThat(uriRef17.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef17.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef17.getPort()).isEqualTo(-1);
        assertThat(uriRef17.getPath()).isEqualTo("/b/c/");
        assertThat(uriRef17.getQuery()).isNull();
        assertThat(uriRef17.getFragment()).isNull();

        var uriRef18 = URIReference.parse("http://a/b/c/d;p?q").resolve("..");
        assertThat(uriRef18.toString()).isEqualTo("http://a/b/");
        assertThat(uriRef18.isRelativeReference()).isFalse();
        assertThat(uriRef18.getScheme()).isEqualTo("http");
        assertThat(uriRef18.hasAuthority()).isTrue();
        assertThat(uriRef18.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef18.getUserinfo()).isNull();
        assertThat(uriRef18.getHost().toString()).isEqualTo("a");
        assertThat(uriRef18.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef18.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef18.getPort()).isEqualTo(-1);
        assertThat(uriRef18.getPath()).isEqualTo("/b/");
        assertThat(uriRef18.getQuery()).isNull();
        assertThat(uriRef18.getFragment()).isNull();

        var uriRef19 = URIReference.parse("http://a/b/c/d;p?q").resolve("../");
        assertThat(uriRef19.toString()).isEqualTo("http://a/b/");
        assertThat(uriRef19.isRelativeReference()).isFalse();
        assertThat(uriRef19.getScheme()).isEqualTo("http");
        assertThat(uriRef19.hasAuthority()).isTrue();
        assertThat(uriRef19.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef19.getUserinfo()).isNull();
        assertThat(uriRef19.getHost().toString()).isEqualTo("a");
        assertThat(uriRef19.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef19.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef19.getPort()).isEqualTo(-1);
        assertThat(uriRef19.getPath()).isEqualTo("/b/");
        assertThat(uriRef19.getQuery()).isNull();
        assertThat(uriRef19.getFragment()).isNull();

        var uriRef20 = URIReference.parse("http://a/b/c/d;p?q").resolve("../g");
        assertThat(uriRef20.toString()).isEqualTo("http://a/b/g");
        assertThat(uriRef20.isRelativeReference()).isFalse();
        assertThat(uriRef20.getScheme()).isEqualTo("http");
        assertThat(uriRef20.hasAuthority()).isTrue();
        assertThat(uriRef20.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef20.getUserinfo()).isNull();
        assertThat(uriRef20.getHost().toString()).isEqualTo("a");
        assertThat(uriRef20.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef20.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef20.getPort()).isEqualTo(-1);
        assertThat(uriRef20.getPath()).isEqualTo("/b/g");
        assertThat(uriRef20.getQuery()).isNull();
        assertThat(uriRef20.getFragment()).isNull();

        var uriRef21 = URIReference.parse("http://a/b/c/d;p?q").resolve("../..");
        assertThat(uriRef21.toString()).isEqualTo("http://a/");
        assertThat(uriRef21.isRelativeReference()).isFalse();
        assertThat(uriRef21.getScheme()).isEqualTo("http");
        assertThat(uriRef21.hasAuthority()).isTrue();
        assertThat(uriRef21.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef21.getUserinfo()).isNull();
        assertThat(uriRef21.getHost().toString()).isEqualTo("a");
        assertThat(uriRef21.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef21.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef21.getPort()).isEqualTo(-1);
        assertThat(uriRef21.getPath()).isEqualTo("/");
        assertThat(uriRef21.getQuery()).isNull();
        assertThat(uriRef21.getFragment()).isNull();

        var uriRef22 = URIReference.parse("http://a/b/c/d;p?q").resolve("../../");
        assertThat(uriRef22.toString()).isEqualTo("http://a/");
        assertThat(uriRef22.isRelativeReference()).isFalse();
        assertThat(uriRef22.getScheme()).isEqualTo("http");
        assertThat(uriRef22.hasAuthority()).isTrue();
        assertThat(uriRef22.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef22.getUserinfo()).isNull();
        assertThat(uriRef22.getHost().toString()).isEqualTo("a");
        assertThat(uriRef22.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef22.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef22.getPort()).isEqualTo(-1);
        assertThat(uriRef22.getPath()).isEqualTo("/");
        assertThat(uriRef22.getQuery()).isNull();
        assertThat(uriRef22.getFragment()).isNull();

        var uriRef23 = URIReference.parse("http://a/b/c/d;p?q").resolve("../../g");
        assertThat(uriRef23.toString()).isEqualTo("http://a/g");
        assertThat(uriRef23.isRelativeReference()).isFalse();
        assertThat(uriRef23.getScheme()).isEqualTo("http");
        assertThat(uriRef23.hasAuthority()).isTrue();
        assertThat(uriRef23.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef23.getUserinfo()).isNull();
        assertThat(uriRef23.getHost().toString()).isEqualTo("a");
        assertThat(uriRef23.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef23.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef23.getPort()).isEqualTo(-1);
        assertThat(uriRef23.getPath()).isEqualTo("/g");
        assertThat(uriRef23.getQuery()).isNull();
        assertThat(uriRef23.getFragment()).isNull();

        var uriRef24 = URIReference.parse("http://a/b/c/d;p?q").resolve("g?y/./x");
        assertThat(uriRef24.toString()).isEqualTo("http://a/b/c/g?y/./x");
        assertThat(uriRef24.isRelativeReference()).isFalse();
        assertThat(uriRef24.getScheme()).isEqualTo("http");
        assertThat(uriRef24.hasAuthority()).isTrue();
        assertThat(uriRef24.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef24.getUserinfo()).isNull();
        assertThat(uriRef24.getHost().toString()).isEqualTo("a");
        assertThat(uriRef24.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef24.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef24.getPort()).isEqualTo(-1);
        assertThat(uriRef24.getPath()).isEqualTo("/b/c/g");
        assertThat(uriRef24.getQuery()).isEqualTo("y/./x");
        assertThat(uriRef24.getFragment()).isNull();

        var uriRef25 = URIReference.parse("http://a/b/c/d;p?q").resolve("g?y/../x");
        assertThat(uriRef25.toString()).isEqualTo("http://a/b/c/g?y/../x");
        assertThat(uriRef25.isRelativeReference()).isFalse();
        assertThat(uriRef25.getScheme()).isEqualTo("http");
        assertThat(uriRef25.hasAuthority()).isTrue();
        assertThat(uriRef25.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef25.getUserinfo()).isNull();
        assertThat(uriRef25.getHost().toString()).isEqualTo("a");
        assertThat(uriRef25.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef25.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef25.getPort()).isEqualTo(-1);
        assertThat(uriRef25.getPath()).isEqualTo("/b/c/g");
        assertThat(uriRef25.getQuery()).isEqualTo("y/../x");
        assertThat(uriRef25.getFragment()).isNull();

        var uriRef26 = URIReference.parse("http://a/b/c/d;p?q").resolve("g#s/./x");
        assertThat(uriRef26.toString()).isEqualTo("http://a/b/c/g#s/./x");
        assertThat(uriRef26.isRelativeReference()).isFalse();
        assertThat(uriRef26.getScheme()).isEqualTo("http");
        assertThat(uriRef26.hasAuthority()).isTrue();
        assertThat(uriRef26.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef26.getUserinfo()).isNull();
        assertThat(uriRef26.getHost().toString()).isEqualTo("a");
        assertThat(uriRef26.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef26.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef26.getPort()).isEqualTo(-1);
        assertThat(uriRef26.getPath()).isEqualTo("/b/c/g");
        assertThat(uriRef26.getQuery()).isNull();
        assertThat(uriRef26.getFragment()).isEqualTo("s/./x");

        var uriRef27 = URIReference.parse("http://a/b/c/d;p?q").resolve("g#s/../x");
        assertThat(uriRef27.toString()).isEqualTo("http://a/b/c/g#s/../x");
        assertThat(uriRef27.isRelativeReference()).isFalse();
        assertThat(uriRef27.getScheme()).isEqualTo("http");
        assertThat(uriRef27.hasAuthority()).isTrue();
        assertThat(uriRef27.getAuthority().toString()).isEqualTo("a");
        assertThat(uriRef27.getUserinfo()).isNull();
        assertThat(uriRef27.getHost().toString()).isEqualTo("a");
        assertThat(uriRef27.getHost().getValue()).isEqualTo("a");
        assertThat(uriRef27.getHost().getType()).isEqualTo(HostType.REGNAME);
        assertThat(uriRef27.getPort()).isEqualTo(-1);
        assertThat(uriRef27.getPath()).isEqualTo("/b/c/g");
        assertThat(uriRef27.getQuery()).isNull();
        assertThat(uriRef27.getFragment()).isEqualTo("s/../x");

        assertThrowsNPE(
            "The input string must not be null.",
            () -> URIReference.parse("http://a/b/c/d;p?q").resolve((String)null));

        assertThrowsISE(
            "The base URI must have a scheme.",
            () -> URIReference.parse("/a/b/c/d;p?q").resolve("g"));

        assertThrowsISE(
            "The base URI must not have a fragment.",
            () -> URIReference.parse("http://a/b/c/d;p?q#s").resolve("g"));
    }


    @Test
    void normalize()
    {
        var uriRef1 = URIReference.parse("hTTp://example.com/").normalize();
        assertThat(uriRef1.toString()).isEqualTo("http://example.com/");
        assertThat(uriRef1.isRelativeReference()).isFalse();
        assertThat(uriRef1.getScheme()).isEqualTo("http");
        assertThat(uriRef1.hasAuthority()).isTrue();
        assertThat(uriRef1.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef1.getUserinfo()).isNull();
        assertThat(uriRef1.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef1.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef1.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef1.getPort()).isEqualTo(-1);
        assertThat(uriRef1.getPath()).isEqualTo("/");
        assertThat(uriRef1.getQuery()).isNull();
        assertThat(uriRef1.getFragment()).isNull();

        var uriRef2 = URIReference.parse("http://example.com/").normalize();
        assertThat(uriRef2.hasAuthority()).isTrue();
        assertThat(uriRef2.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef2.getUserinfo()).isNull();
        assertThat(uriRef2.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef2.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef2.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef2.getPort()).isEqualTo(-1);
        assertThat(uriRef2.getQuery()).isNull();
        assertThat(uriRef2.getFragment()).isNull();

        var uriRef3 = URIReference.parse("http://%75ser@example.com/").normalize();
        assertThat(uriRef3.toString()).isEqualTo("http://user@example.com/");
        assertThat(uriRef3.isRelativeReference()).isFalse();
        assertThat(uriRef3.getScheme()).isEqualTo("http");
        assertThat(uriRef3.hasAuthority()).isTrue();
        assertThat(uriRef3.getAuthority().toString()).isEqualTo("user@example.com");
        assertThat(uriRef3.getUserinfo()).isEqualTo("user");
        assertThat(uriRef3.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef3.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef3.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef3.getPort()).isEqualTo(-1);
        assertThat(uriRef3.getPath()).isEqualTo("/");
        assertThat(uriRef3.getQuery()).isNull();
        assertThat(uriRef3.getFragment()).isNull();

        var uriRef4 = URIReference.parse("http://%e3%83%a6%e3%83%bc%e3%82%b6%e3%83%bc@example.com/").normalize();
        assertThat(uriRef4.toString()).isEqualTo("http://%E3%83%A6%E3%83%BC%E3%82%B6%E3%83%BC@example.com/");
        assertThat(uriRef4.isRelativeReference()).isFalse();
        assertThat(uriRef4.getScheme()).isEqualTo("http");
        assertThat(uriRef4.hasAuthority()).isTrue();
        assertThat(uriRef4.getAuthority().toString()).isEqualTo("%E3%83%A6%E3%83%BC%E3%82%B6%E3%83%BC@example.com");
        assertThat(uriRef4.getUserinfo()).isEqualTo("%E3%83%A6%E3%83%BC%E3%82%B6%E3%83%BC");
        assertThat(uriRef4.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef4.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef4.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef4.getPort()).isEqualTo(-1);
        assertThat(uriRef4.getPath()).isEqualTo("/");
        assertThat(uriRef4.getQuery()).isNull();
        assertThat(uriRef4.getFragment()).isNull();

        var uriRef5 = URIReference.parse("http://%65%78%61%6D%70%6C%65.com/").normalize();
        assertThat(uriRef5.toString()).isEqualTo("http://example.com/");
        assertThat(uriRef5.isRelativeReference()).isFalse();
        assertThat(uriRef5.getScheme()).isEqualTo("http");
        assertThat(uriRef5.hasAuthority()).isTrue();
        assertThat(uriRef5.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef5.getUserinfo()).isNull();
        assertThat(uriRef5.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef5.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef5.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef5.getPort()).isEqualTo(-1);
        assertThat(uriRef5.getPath()).isEqualTo("/");
        assertThat(uriRef5.getQuery()).isNull();
        assertThat(uriRef5.getFragment()).isNull();

        var uriRef6 = URIReference.parse("http://%e4%be%8b.com/").normalize();
        assertThat(uriRef6.toString()).isEqualTo("http://%E4%BE%8B.com/");
        assertThat(uriRef6.isRelativeReference()).isFalse();
        assertThat(uriRef6.getScheme()).isEqualTo("http");
        assertThat(uriRef6.hasAuthority()).isTrue();
        assertThat(uriRef6.getAuthority().toString()).isEqualTo("%E4%BE%8B.com");
        assertThat(uriRef6.getUserinfo()).isNull();
        assertThat(uriRef6.getHost().toString()).isEqualTo("%E4%BE%8B.com");
        assertThat(uriRef6.getHost().getValue()).isEqualTo("%E4%BE%8B.com");
        assertThat(uriRef6.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef6.getPort()).isEqualTo(-1);
        assertThat(uriRef6.getPath()).isEqualTo("/");
        assertThat(uriRef6.getQuery()).isNull();
        assertThat(uriRef6.getFragment()).isNull();

        var uriRef7 = URIReference.parse("http://LOCALhost/").normalize();
        assertThat(uriRef7.toString()).isEqualTo("http://localhost/");
        assertThat(uriRef7.isRelativeReference()).isFalse();
        assertThat(uriRef7.getScheme()).isEqualTo("http");
        assertThat(uriRef7.hasAuthority()).isTrue();
        assertThat(uriRef7.getAuthority().toString()).isEqualTo("localhost");
        assertThat(uriRef7.getUserinfo()).isNull();
        assertThat(uriRef7.getHost().toString()).isEqualTo("localhost");
        assertThat(uriRef7.getHost().getValue()).isEqualTo("localhost");
        assertThat(uriRef7.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef7.getPort()).isEqualTo(-1);
        assertThat(uriRef7.getPath()).isEqualTo("/");
        assertThat(uriRef7.getQuery()).isNull();
        assertThat(uriRef7.getFragment()).isNull();

        var uriRef8 = URIReference.parse("http://example.com").normalize();
        assertThat(uriRef8.toString()).isEqualTo("http://example.com/");
        assertThat(uriRef8.isRelativeReference()).isFalse();
        assertThat(uriRef8.getScheme()).isEqualTo("http");
        assertThat(uriRef8.hasAuthority()).isTrue();
        assertThat(uriRef8.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef8.getUserinfo()).isNull();
        assertThat(uriRef8.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef8.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef8.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef8.getPort()).isEqualTo(-1);
        assertThat(uriRef8.getPath()).isEqualTo("/");
        assertThat(uriRef8.getQuery()).isNull();
        assertThat(uriRef8.getFragment()).isNull();

        var uriRef9 = URIReference.parse("http://example.com/%61/%62/%63/").normalize();
        assertThat(uriRef9.toString()).isEqualTo("http://example.com/a/b/c/");
        assertThat(uriRef9.isRelativeReference()).isFalse();
        assertThat(uriRef9.getScheme()).isEqualTo("http");
        assertThat(uriRef9.hasAuthority()).isTrue();
        assertThat(uriRef9.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef9.getUserinfo()).isNull();
        assertThat(uriRef9.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef9.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef9.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef9.getPort()).isEqualTo(-1);
        assertThat(uriRef9.getPath()).isEqualTo("/a/b/c/");
        assertThat(uriRef9.getQuery()).isNull();
        assertThat(uriRef9.getFragment()).isNull();

        var uriRef10 = URIReference.parse("http://example.com/%e3%83%91%e3%82%b9/").normalize();
        assertThat(uriRef10.toString()).isEqualTo("http://example.com/%E3%83%91%E3%82%B9/");
        assertThat(uriRef10.isRelativeReference()).isFalse();
        assertThat(uriRef10.getScheme()).isEqualTo("http");
        assertThat(uriRef10.hasAuthority()).isTrue();
        assertThat(uriRef10.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef10.getUserinfo()).isNull();
        assertThat(uriRef10.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef10.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef10.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef10.getPort()).isEqualTo(-1);
        assertThat(uriRef10.getPath()).isEqualTo("/%E3%83%91%E3%82%B9/");
        assertThat(uriRef10.getQuery()).isNull();
        assertThat(uriRef10.getFragment()).isNull();

        var uriRef11 = URIReference.parse("http://example.com/a/b/c/../d/").normalize();
        assertThat(uriRef11.toString()).isEqualTo("http://example.com/a/b/d/");
        assertThat(uriRef11.isRelativeReference()).isFalse();
        assertThat(uriRef11.getScheme()).isEqualTo("http");
        assertThat(uriRef11.hasAuthority()).isTrue();
        assertThat(uriRef11.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef11.getUserinfo()).isNull();
        assertThat(uriRef11.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef11.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef11.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef11.getPort()).isEqualTo(-1);
        assertThat(uriRef11.getPath()).isEqualTo("/a/b/d/");
        assertThat(uriRef11.getQuery()).isNull();
        assertThat(uriRef11.getFragment()).isNull();

        var uriRef12 = URIReference.parse("http://example.com:80/").normalize();
        assertThat(uriRef12.toString()).isEqualTo("http://example.com/");
        assertThat(uriRef12.isRelativeReference()).isFalse();
        assertThat(uriRef12.getScheme()).isEqualTo("http");
        assertThat(uriRef12.hasAuthority()).isTrue();
        assertThat(uriRef12.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef12.getUserinfo()).isNull();
        assertThat(uriRef12.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef12.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef12.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef12.getPort()).isEqualTo(-1);
        assertThat(uriRef12.getPath()).isEqualTo("/");
        assertThat(uriRef12.getQuery()).isNull();
        assertThat(uriRef12.getFragment()).isNull();

        assertThrowsISE(
            "A relative references must be resolved before it can be normalized.",
            () -> URIReference.parse("//example.com").normalize());
    }
}
