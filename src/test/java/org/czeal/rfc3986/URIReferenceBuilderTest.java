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


import static org.czeal.rfc3986.TestUtils.assertThrowsNPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.czeal.rfc3986.HostType.IPV4;
import static org.czeal.rfc3986.HostType.IPV6;
import static org.czeal.rfc3986.HostType.IPVFUTURE;
import static org.czeal.rfc3986.HostType.REGNAME;
import org.junit.jupiter.api.Test;


class URIReferenceBuilderTest
{
    @Test
    void from_urireference_with_string()
    {
        var uriRef1 = URIReferenceBuilder.fromURIReference("http://example.com").build();
        assertThat(uriRef1.toString()).isEqualTo("http://example.com");
        assertThat(uriRef1.isRelativeReference()).isFalse();
        assertThat(uriRef1.hasAuthority()).isTrue();
        assertThat(uriRef1.getScheme()).isEqualTo("http");
        var authority1 = uriRef1.getAuthority();
        assertThat(authority1.toString()).isEqualTo("example.com");
        assertThat(authority1.getUserinfo()).isNull();
        var host1 = authority1.getHost();
        assertThat(host1.toString()).isEqualTo("example.com");
        assertThat(host1.getValue()).isEqualTo("example.com");
        assertThat(host1.getType()).isEqualTo(REGNAME);
        assertThat(authority1.getPort()).isEqualTo(-1);
        assertThat(uriRef1.getPath()).isEqualTo("");
        assertThat(uriRef1.getQuery()).isNull();
        assertThat(uriRef1.getFragment()).isNull();

        var uriRef2 = URIReferenceBuilder.fromURIReference("hTTp://example.com").build();;
        assertThat(uriRef2.toString()).isEqualTo("hTTp://example.com");
        assertThat(uriRef2.isRelativeReference()).isFalse();
        assertThat(uriRef2.hasAuthority()).isTrue();
        assertThat(uriRef2.getScheme()).isEqualTo("hTTp");
        var authority2 = uriRef2.getAuthority();
        assertThat(authority2.toString()).isEqualTo("example.com");
        assertThat(authority2.getUserinfo()).isNull();
        var host2 = authority2.getHost();
        assertThat(host2.toString()).isEqualTo("example.com");
        assertThat(host2.getValue()).isEqualTo("example.com");
        assertThat(host2.getType()).isEqualTo(REGNAME);
        assertThat(authority2.getPort()).isEqualTo(-1);
        assertThat(uriRef2.getPath()).isEqualTo("");
        assertThat(uriRef2.getQuery()).isNull();
        assertThat(uriRef2.getFragment()).isNull();

        var uriRef3 = URIReferenceBuilder.fromURIReference("//example.com").build();;
        assertThat(uriRef3.toString()).isEqualTo("//example.com");
        assertThat(uriRef3.isRelativeReference()).isTrue();
        assertThat(uriRef3.hasAuthority()).isTrue();
        assertThat(uriRef3.getScheme()).isNull();
        var authority = uriRef3.getAuthority();
        assertThat(authority.toString()).isEqualTo("example.com");
        assertThat(authority.getUserinfo()).isNull();
        var host = authority.getHost();
        assertThat(host.toString()).isEqualTo("example.com");
        assertThat(host.getValue()).isEqualTo("example.com");
        assertThat(host.getType()).isEqualTo(REGNAME);
        assertThat(authority.getPort()).isEqualTo(-1);
        assertThat(uriRef3.getPath()).isEqualTo("");
        assertThat(uriRef3.getQuery()).isNull();
        assertThat(uriRef3.getFragment()).isNull();

        var uriRef4 = URIReferenceBuilder.fromURIReference("http:").setAuthorityRequired(false).build();;
        assertThat(uriRef4.isRelativeReference()).isFalse();
        assertThat(uriRef4.hasAuthority()).isFalse();
        assertThat(uriRef4.getScheme()).isEqualTo("http");
        assertThat(uriRef4.getAuthority()).isNull();
        assertThat(uriRef4.getPath()).isEqualTo("");
        assertThat(uriRef4.getQuery()).isNull();
        assertThat(uriRef4.getFragment()).isNull();

        var uriRef5 = URIReferenceBuilder.fromURIReference("http://john@example.com").build();;
        assertThat(uriRef5.toString()).isEqualTo("http://john@example.com");
        assertThat(uriRef5.isRelativeReference()).isFalse();
        assertThat(uriRef5.hasAuthority()).isTrue();
        assertThat(uriRef5.getScheme()).isEqualTo("http");
        var authority5 = uriRef5.getAuthority();
        assertThat(authority5.toString()).isEqualTo("john@example.com");
        assertThat(authority5.getUserinfo()).isEqualTo("john");
        var host5 = authority5.getHost();
        assertThat(host5.toString()).isEqualTo("example.com");
        assertThat(host5.getValue()).isEqualTo("example.com");
        assertThat(host5.getType()).isEqualTo(REGNAME);
        assertThat(authority5.getPort()).isEqualTo(-1);
        assertThat(uriRef5.getPath()).isEqualTo("");
        assertThat(uriRef5.getQuery()).isNull();
        assertThat(uriRef5.getFragment()).isNull();

        var uriRef6 = URIReferenceBuilder.fromURIReference("http://%6A%6F%68%6E@example.com").build();;
        assertThat(uriRef6.toString()).isEqualTo("http://%6A%6F%68%6E@example.com");
        assertThat(uriRef6.isRelativeReference()).isFalse();
        assertThat(uriRef6.hasAuthority()).isTrue();
        assertThat(uriRef6.getScheme()).isEqualTo("http");
        var authority6 = uriRef6.getAuthority();
        assertThat(authority6.toString()).isEqualTo("%6A%6F%68%6E@example.com");
        assertThat(authority6.getUserinfo()).isEqualTo("%6A%6F%68%6E");
        var host6 = authority6.getHost();
        assertThat(host6.toString()).isEqualTo("example.com");
        assertThat(host6.getValue()).isEqualTo("example.com");
        assertThat(host6.getType()).isEqualTo(REGNAME);
        assertThat(authority6.getPort()).isEqualTo(-1);
        assertThat(uriRef6.getPath()).isEqualTo("");
        assertThat(uriRef6.getQuery()).isNull();
        assertThat(uriRef6.getFragment()).isNull();

        var uriRef7 = URIReferenceBuilder.fromURIReference("http://101.102.103.104").build();;
        assertThat(uriRef7.toString()).isEqualTo("http://101.102.103.104");
        assertThat(uriRef7.isRelativeReference()).isFalse();
        assertThat(uriRef7.hasAuthority()).isTrue();
        assertThat(uriRef7.getScheme()).isEqualTo("http");
        var authority7 = uriRef7.getAuthority();
        assertThat(authority7.toString()).isEqualTo("101.102.103.104");
        assertThat(authority7.getUserinfo()).isNull();
        var host7 = authority7.getHost();
        assertThat(host7.toString()).isEqualTo("101.102.103.104");
        assertThat(host7.getValue()).isEqualTo("101.102.103.104");
        assertThat(host7.getType()).isEqualTo(HostType.IPV4);
        assertThat(authority7.getPort()).isEqualTo(-1);
        assertThat(uriRef7.getPath()).isEqualTo("");
        assertThat(uriRef7.getQuery()).isNull();
        assertThat(uriRef7.getFragment()).isNull();

        var uriRef8 = URIReferenceBuilder.fromURIReference("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").build();;
        assertThat(uriRef8.toString()).isEqualTo("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
        assertThat(uriRef8.isRelativeReference()).isFalse();
        assertThat(uriRef8.hasAuthority()).isTrue();
        assertThat(uriRef8.getScheme()).isEqualTo("http");
        var authority8 = uriRef8.getAuthority();
        assertThat(authority8.toString()).isEqualTo("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
        assertThat(authority8.getUserinfo()).isNull();
        var host8 = authority8.getHost();
        assertThat(host8.toString()).isEqualTo("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
        assertThat(host8.getValue()).isEqualTo("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
        assertThat(host8.getType()).isEqualTo(HostType.IPV6);
        assertThat(authority8.getPort()).isEqualTo(-1);
        assertThat(uriRef8.getPath()).isEqualTo("");
        assertThat(uriRef8.getQuery()).isNull();
        assertThat(uriRef8.getFragment()).isNull();

        var uriRef9 = URIReferenceBuilder.fromURIReference("http://[2001:db8:0:1:1:1:1:1]").build();;
        assertThat(uriRef9.toString()).isEqualTo("http://[2001:db8:0:1:1:1:1:1]");
        assertThat(uriRef9.isRelativeReference()).isFalse();
        assertThat(uriRef9.hasAuthority()).isTrue();
        assertThat(uriRef9.getScheme()).isEqualTo("http");
        var authority9 = uriRef9.getAuthority();
        assertThat(authority9.toString()).isEqualTo("[2001:db8:0:1:1:1:1:1]");
        assertThat(authority9.getUserinfo()).isNull();
        var host9 = authority9.getHost();
        assertThat(host9.toString()).isEqualTo("[2001:db8:0:1:1:1:1:1]");
        assertThat(host9.getValue()).isEqualTo("[2001:db8:0:1:1:1:1:1]");
        assertThat(host9.getType()).isEqualTo(HostType.IPV6);
        assertThat(authority9.getPort()).isEqualTo(-1);
        assertThat(uriRef9.getPath()).isEqualTo("");
        assertThat(uriRef9.getQuery()).isNull();
        assertThat(uriRef9.getFragment()).isNull();

        var uriRef10 = URIReferenceBuilder.fromURIReference("http://[2001:0:9d38:6abd:0:0:0:42]").build();;
        assertThat(uriRef10.toString()).isEqualTo("http://[2001:0:9d38:6abd:0:0:0:42]");
        assertThat(uriRef10.isRelativeReference()).isFalse();
        assertThat(uriRef10.hasAuthority()).isTrue();
        assertThat(uriRef10.getScheme()).isEqualTo("http");
        var authority10 = uriRef10.getAuthority();
        assertThat(authority10.toString()).isEqualTo("[2001:0:9d38:6abd:0:0:0:42]");
        assertThat(authority10.getUserinfo()).isNull();
        var host10 = authority10.getHost();
        assertThat(host10.toString()).isEqualTo("[2001:0:9d38:6abd:0:0:0:42]");
        assertThat(host10.getValue()).isEqualTo("[2001:0:9d38:6abd:0:0:0:42]");
        assertThat(host10.getType()).isEqualTo(HostType.IPV6);
        assertThat(authority10.getPort()).isEqualTo(-1);
        assertThat(uriRef10.getPath()).isEqualTo("");
        assertThat(uriRef10.getQuery()).isNull();
        assertThat(uriRef10.getFragment()).isNull();

        var uriRef11 = URIReferenceBuilder.fromURIReference("http://[fe80::1]").build();;
        assertThat(uriRef11.toString()).isEqualTo("http://[fe80::1]");
        assertThat(uriRef11.isRelativeReference()).isFalse();
        assertThat(uriRef11.hasAuthority()).isTrue();
        assertThat(uriRef11.getScheme()).isEqualTo("http");
        var authority11 = uriRef11.getAuthority();
        assertThat(authority11.toString()).isEqualTo("[fe80::1]");
        assertThat(authority11.getUserinfo()).isNull();
        var host11 = authority11.getHost();
        assertThat(host11.toString()).isEqualTo("[fe80::1]");
        assertThat(host11.getValue()).isEqualTo("[fe80::1]");
        assertThat(host11.getType()).isEqualTo(HostType.IPV6);
        assertThat(authority11.getPort()).isEqualTo(-1);
        assertThat(uriRef11.getPath()).isEqualTo("");
        assertThat(uriRef11.getQuery()).isNull();
        assertThat(uriRef11.getFragment()).isNull();

        var uriRef12 = URIReferenceBuilder.fromURIReference("http://[2001:0:3238:DFE1:63::FEFB]").build();;
        assertThat(uriRef12.toString()).isEqualTo("http://[2001:0:3238:DFE1:63::FEFB]");
        assertThat(uriRef12.isRelativeReference()).isFalse();
        assertThat(uriRef12.hasAuthority()).isTrue();
        assertThat(uriRef12.getScheme()).isEqualTo("http");
        var authority12 = uriRef12.getAuthority();
        assertThat(authority12.toString()).isEqualTo("[2001:0:3238:DFE1:63::FEFB]");
        assertThat(authority12.getUserinfo()).isNull();
        var host12 = authority12.getHost();
        assertThat(host12.toString()).isEqualTo("[2001:0:3238:DFE1:63::FEFB]");
        assertThat(host12.getValue()).isEqualTo("[2001:0:3238:DFE1:63::FEFB]");
        assertThat(host12.getType()).isEqualTo(HostType.IPV6);
        assertThat(authority12.getPort()).isEqualTo(-1);
        assertThat(uriRef12.getPath()).isEqualTo("");
        assertThat(uriRef12.getQuery()).isNull();
        assertThat(uriRef12.getFragment()).isNull();

        var uriRef13 = URIReferenceBuilder.fromURIReference("http://[v1.fe80::a+en1]").build();;
        assertThat(uriRef13.toString()).isEqualTo("http://[v1.fe80::a+en1]");
        assertThat(uriRef13.isRelativeReference()).isFalse();
        assertThat(uriRef13.hasAuthority()).isTrue();
        assertThat(uriRef13.getScheme()).isEqualTo("http");
        var authority13 = uriRef13.getAuthority();
        assertThat(authority13.toString()).isEqualTo("[v1.fe80::a+en1]");
        assertThat(authority13.getUserinfo()).isNull();
        var host13 = authority13.getHost();
        assertThat(host13.toString()).isEqualTo("[v1.fe80::a+en1]");
        assertThat(host13.getValue()).isEqualTo("[v1.fe80::a+en1]");
        assertThat(host13.getType()).isEqualTo(HostType.IPVFUTURE);
        assertThat(authority13.getPort()).isEqualTo(-1);
        assertThat(uriRef13.getPath()).isEqualTo("");
        assertThat(uriRef13.getQuery()).isNull();
        assertThat(uriRef13.getFragment()).isNull();

        var uriRef14 = URIReferenceBuilder.fromURIReference("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D").build();;
        assertThat(uriRef14.toString()).isEqualTo("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D");
        assertThat(uriRef14.isRelativeReference()).isFalse();
        assertThat(uriRef14.hasAuthority()).isTrue();
        assertThat(uriRef14.getScheme()).isEqualTo("http");
        var authority14 = uriRef14.getAuthority();
        assertThat(authority14.toString()).isEqualTo("%65%78%61%6D%70%6C%65%2E%63%6F%6D");
        assertThat(authority14.getUserinfo()).isNull();
        var host14 = authority14.getHost();
        assertThat(host14.toString()).isEqualTo("%65%78%61%6D%70%6C%65%2E%63%6F%6D");
        assertThat(host14.getValue()).isEqualTo("%65%78%61%6D%70%6C%65%2E%63%6F%6D");
        assertThat(host14.getType()).isEqualTo(REGNAME);
        assertThat(authority14.getPort()).isEqualTo(-1);
        assertThat(uriRef14.getPath()).isEqualTo("");
        assertThat(uriRef14.getQuery()).isNull();
        assertThat(uriRef14.getFragment()).isNull();

        var uriRef15 = URIReferenceBuilder.fromURIReference("http://").build();;
        assertThat(uriRef15.isRelativeReference()).isFalse();
        assertThat(uriRef15.hasAuthority()).isTrue();
        assertThat(uriRef15.getScheme()).isEqualTo("http");
        var authority15 = uriRef15.getAuthority();
        assertThat(authority15.getUserinfo()).isNull();
        var host15 = authority15.getHost();
        assertThat(host15.getValue()).isEqualTo("");
        assertThat(host15.toString()).isEqualTo("");
        assertThat(host15.getType()).isEqualTo(REGNAME);
        assertThat(authority15.getPort()).isEqualTo(-1);
        assertThat(uriRef15.getPath()).isEqualTo("");
        assertThat(uriRef15.getQuery()).isNull();
        assertThat(uriRef15.getFragment()).isNull();

        var uriRef16 = URIReferenceBuilder.fromURIReference("http:///a").build();;
        assertThat(uriRef16.isRelativeReference()).isFalse();
        assertThat(uriRef16.hasAuthority()).isTrue();
        assertThat(uriRef16.getScheme()).isEqualTo("http");
        var authority16 = uriRef16.getAuthority();
        assertThat(authority16.getUserinfo()).isNull();
        var host16 = authority16.getHost();
        assertThat(host16.toString()).isEqualTo("");
        assertThat(host16.getValue()).isEqualTo("");
        assertThat(host16.getType()).isEqualTo(REGNAME);
        assertThat(authority16.getPort()).isEqualTo(-1);
        assertThat(uriRef16.getPath()).isEqualTo("/a");
        assertThat(uriRef16.getQuery()).isNull();
        assertThat(uriRef16.getFragment()).isNull();

        var uriRef17 = URIReferenceBuilder.fromURIReference("http://example.com:80").build();;
        assertThat(uriRef17.isRelativeReference()).isFalse();
        assertThat(uriRef17.hasAuthority()).isTrue();
        assertThat(uriRef17.getScheme()).isEqualTo("http");
        var authority17 = uriRef17.getAuthority();
        assertThat(authority17.getUserinfo()).isNull();
        var host17 = authority17.getHost();
        assertThat(host17.toString()).isEqualTo("example.com");
        assertThat(host17.getValue()).isEqualTo("example.com");
        assertThat(host17.getType()).isEqualTo(REGNAME);
        assertThat(authority17.getPort()).isEqualTo(80);
        assertThat(uriRef17.getPath()).isEqualTo("");
        assertThat(uriRef17.getQuery()).isNull();
        assertThat(uriRef17.getFragment()).isNull();

        var uriRef18 = URIReferenceBuilder.fromURIReference("http://example.com:").build();;
        assertThat(uriRef18.isRelativeReference()).isFalse();
        assertThat(uriRef18.hasAuthority()).isTrue();
        assertThat(uriRef18.getScheme()).isEqualTo("http");
        var authority18 = uriRef18.getAuthority();
        assertThat(authority18.getUserinfo()).isNull();
        var host18 = authority18.getHost();
        assertThat(host18.toString()).isEqualTo("example.com");
        assertThat(host18.getValue()).isEqualTo("example.com");
        assertThat(host18.getType()).isEqualTo(REGNAME);
        assertThat(authority18.getPort()).isEqualTo(-1);
        assertThat(uriRef18.getPath()).isEqualTo("");
        assertThat(uriRef18.getQuery()).isNull();
        assertThat(uriRef18.getFragment()).isNull();

        var uriRef19 = URIReferenceBuilder.fromURIReference("http://example.com:001").build();;
        assertThat(uriRef19.isRelativeReference()).isFalse();
        assertThat(uriRef19.hasAuthority()).isTrue();
        assertThat(uriRef19.getScheme()).isEqualTo("http");
        var authority19 = uriRef19.getAuthority();
        assertThat(authority19.getUserinfo()).isNull();
        var host19 = authority19.getHost();
        assertThat(host19.toString()).isEqualTo("example.com");
        assertThat(host19.getValue()).isEqualTo("example.com");
        assertThat(host19.getType()).isEqualTo(REGNAME);
        assertThat(authority19.getPort()).isEqualTo(1);
        assertThat(uriRef19.getPath()).isEqualTo("");
        assertThat(uriRef19.getQuery()).isNull();
        assertThat(uriRef19.getFragment()).isNull();

        var uriRef20 = URIReferenceBuilder.fromURIReference("http://example.com/a/b/c").build();;
        assertThat(uriRef20.toString()).isEqualTo("http://example.com/a/b/c");
        assertThat(uriRef20.isRelativeReference()).isFalse();
        assertThat(uriRef20.hasAuthority()).isTrue();
        assertThat(uriRef20.getScheme()).isEqualTo("http");
        var authority20 = uriRef20.getAuthority();
        assertThat(authority20.toString()).isEqualTo("example.com");
        assertThat(authority20.getUserinfo()).isNull();
        var host20 = authority20.getHost();
        assertThat(host20.toString()).isEqualTo("example.com");
        assertThat(host20.getValue()).isEqualTo("example.com");
        assertThat(host20.getType()).isEqualTo(REGNAME);
        assertThat(authority20.getPort()).isEqualTo(-1);
        assertThat(uriRef20.getPath()).isEqualTo("/a/b/c");
        assertThat(uriRef20.getQuery()).isNull();
        assertThat(uriRef20.getFragment()).isNull();

        var uriRef21 = URIReferenceBuilder.fromURIReference("http://example.com/%61/%62/%63").build();;
        assertThat(uriRef21.toString()).isEqualTo("http://example.com/%61/%62/%63");
        assertThat(uriRef21.isRelativeReference()).isFalse();
        assertThat(uriRef21.hasAuthority()).isTrue();
        assertThat(uriRef21.getScheme()).isEqualTo("http");
        var authority21 = uriRef21.getAuthority();
        assertThat(authority21.toString()).isEqualTo("example.com");
        assertThat(authority21.getUserinfo()).isNull();
        var host21 = authority21.getHost();
        assertThat(host21.toString()).isEqualTo("example.com");
        assertThat(host21.getValue()).isEqualTo("example.com");
        assertThat(host21.getType()).isEqualTo(REGNAME);
        assertThat(authority21.getPort()).isEqualTo(-1);
        assertThat(uriRef21.getPath()).isEqualTo("/%61/%62/%63");
        assertThat(uriRef21.getQuery()).isNull();
        assertThat(uriRef21.getFragment()).isNull();

        var uriRef22 = URIReferenceBuilder.fromURIReference("http:/a").setAuthorityRequired(false).build();;
        assertThat(uriRef22.isRelativeReference()).isFalse();
        assertThat(uriRef22.hasAuthority()).isFalse();
        assertThat(uriRef22.getScheme()).isEqualTo("http");
        assertThat(uriRef22.getAuthority()).isNull();
        assertThat(uriRef22.getPath()).isEqualTo("/a");
        assertThat(uriRef22.getQuery()).isNull();
        assertThat(uriRef22.getFragment()).isNull();

        var uriRef23 = URIReferenceBuilder.fromURIReference("http:a").setAuthorityRequired(false).build();;
        assertThat(uriRef23.isRelativeReference()).isFalse();
        assertThat(uriRef23.hasAuthority()).isFalse();
        assertThat(uriRef23.getScheme()).isEqualTo("http");
        assertThat(uriRef23.getAuthority()).isNull();
        assertThat(uriRef23.getPath()).isEqualTo("a");
        assertThat(uriRef23.getQuery()).isNull();
        assertThat(uriRef23.getFragment()).isNull();

        var uriRef24 = URIReferenceBuilder.fromURIReference("//").build();;
        assertThat(uriRef24.isRelativeReference()).isTrue();
        assertThat(uriRef24.hasAuthority()).isTrue();
        assertThat(uriRef24.getScheme()).isNull();
        var authority24 = uriRef24.getAuthority();
        assertThat(authority24.toString()).isEqualTo("");
        assertThat(authority24.getUserinfo()).isNull();
        var host24 = authority24.getHost();
        assertThat(host24.toString()).isEqualTo("");
        assertThat(host24.getValue()).isEqualTo("");
        assertThat(host24.getType()).isEqualTo(REGNAME);
        assertThat(authority24.getPort()).isEqualTo(-1);
        assertThat(uriRef24.getPath()).isEqualTo("");
        assertThat(uriRef24.getQuery()).isNull();
        assertThat(uriRef24.getFragment()).isNull();

        assertThrowsNPE(
            "The input string must not be null.",
            () -> URIReferenceBuilder.fromURIReference((String)null).build());
    }


    @Test
    void from_urireference_with_uri_reference()
    {
        var uriRef1 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://example.com")).build();;
        assertThat(uriRef1.toString()).isEqualTo("http://example.com");
        assertThat(uriRef1.isRelativeReference()).isFalse();
        assertThat(uriRef1.hasAuthority()).isTrue();
        assertThat(uriRef1.getScheme()).isEqualTo("http");
        var authority1 = uriRef1.getAuthority();
        assertThat(authority1.toString()).isEqualTo("example.com");
        assertThat(authority1.getUserinfo()).isNull();
        var host1 = authority1.getHost();
        assertThat(host1.toString()).isEqualTo("example.com");
        assertThat(host1.getValue()).isEqualTo("example.com");
        assertThat(host1.getType()).isEqualTo(REGNAME);
        assertThat(authority1.getPort()).isEqualTo(-1);
        assertThat(uriRef1.getPath()).isEqualTo("");
        assertThat(uriRef1.getQuery()).isNull();
        assertThat(uriRef1.getFragment()).isNull();

        var uriRef2 = URIReferenceBuilder.fromURIReference(URIReference.parse("hTTp://example.com")).build();
        assertThat(uriRef2.toString()).isEqualTo("hTTp://example.com");
        assertThat(uriRef2.isRelativeReference()).isFalse();
        assertThat(uriRef2.hasAuthority()).isTrue();
        assertThat(uriRef2.getScheme()).isEqualTo("hTTp");
        var authority2 = uriRef2.getAuthority();
        assertThat(authority2.toString()).isEqualTo("example.com");
        assertThat(authority2.getUserinfo()).isNull();
        var host2 = authority2.getHost();
        assertThat(host2.toString()).isEqualTo("example.com");
        assertThat(host2.getValue()).isEqualTo("example.com");
        assertThat(host2.getType()).isEqualTo(REGNAME);
        assertThat(authority2.getPort()).isEqualTo(-1);
        assertThat(uriRef2.getPath()).isEqualTo("");
        assertThat(uriRef2.getQuery()).isNull();
        assertThat(uriRef2.getFragment()).isNull();

        var uriRef3 = URIReferenceBuilder.fromURIReference(URIReference.parse("//example.com")).build();
        assertThat(uriRef3.toString()).isEqualTo("//example.com");
        assertThat(uriRef3.isRelativeReference()).isTrue();
        assertThat(uriRef3.hasAuthority()).isTrue();
        assertThat(uriRef3.getScheme()).isNull();
        var authority = uriRef3.getAuthority();
        assertThat(authority.toString()).isEqualTo("example.com");
        assertThat(authority.getUserinfo()).isNull();
        var host = authority.getHost();
        assertThat(host.toString()).isEqualTo("example.com");
        assertThat(host.getValue()).isEqualTo("example.com");
        assertThat(host.getType()).isEqualTo(REGNAME);
        assertThat(authority.getPort()).isEqualTo(-1);
        assertThat(uriRef3.getPath()).isEqualTo("");
        assertThat(uriRef3.getQuery()).isNull();
        assertThat(uriRef3.getFragment()).isNull();

        var uriRef4 = URIReferenceBuilder.fromURIReference(URIReference.parse("http:")).setAuthorityRequired(false).build();
        assertThat(uriRef4.isRelativeReference()).isFalse();
        assertThat(uriRef4.hasAuthority()).isFalse();
        assertThat(uriRef4.getScheme()).isEqualTo("http");
        assertThat(uriRef4.hasAuthority()).isFalse();
        assertThat(uriRef4.getAuthority()).isNull();
        assertThat(uriRef4.getPath()).isEqualTo("");
        assertThat(uriRef4.getQuery()).isNull();
        assertThat(uriRef4.getFragment()).isNull();

        var uriRef5 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://john@example.com")).build();
        assertThat(uriRef5.toString()).isEqualTo("http://john@example.com");
        assertThat(uriRef5.isRelativeReference()).isFalse();
        assertThat(uriRef5.hasAuthority()).isTrue();
        assertThat(uriRef5.getScheme()).isEqualTo("http");
        var authority5 = uriRef5.getAuthority();
        assertThat(authority5.toString()).isEqualTo("john@example.com");
        assertThat(authority5.getUserinfo()).isEqualTo("john");
        var host5 = authority5.getHost();
        assertThat(host5.toString()).isEqualTo("example.com");
        assertThat(host5.getValue()).isEqualTo("example.com");
        assertThat(host5.getType()).isEqualTo(REGNAME);
        assertThat(authority5.getPort()).isEqualTo(-1);
        assertThat(uriRef5.getPath()).isEqualTo("");
        assertThat(uriRef5.getQuery()).isNull();
        assertThat(uriRef5.getFragment()).isNull();

        var uriRef6 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://%6A%6F%68%6E@example.com")).build();
        assertThat(uriRef6.toString()).isEqualTo("http://%6A%6F%68%6E@example.com");
        assertThat(uriRef6.isRelativeReference()).isFalse();
        assertThat(uriRef6.hasAuthority()).isTrue();
        assertThat(uriRef6.getScheme()).isEqualTo("http");
        var authority6 = uriRef6.getAuthority();
        assertThat(authority6.toString()).isEqualTo("%6A%6F%68%6E@example.com");
        assertThat(authority6.getUserinfo()).isEqualTo("%6A%6F%68%6E");
        var host6 = authority6.getHost();
        assertThat(host6.toString()).isEqualTo("example.com");
        assertThat(host6.getValue()).isEqualTo("example.com");
        assertThat(host6.getType()).isEqualTo(REGNAME);
        assertThat(authority6.getPort()).isEqualTo(-1);
        assertThat(uriRef6.getPath()).isEqualTo("");
        assertThat(uriRef6.getQuery()).isNull();
        assertThat(uriRef6.getFragment()).isNull();

        var uriRef7 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://101.102.103.104")).build();
        assertThat(uriRef7.toString()).isEqualTo("http://101.102.103.104");
        assertThat(uriRef7.isRelativeReference()).isFalse();
        assertThat(uriRef7.hasAuthority()).isTrue();
        assertThat(uriRef7.getScheme()).isEqualTo("http");
        var authority7 = uriRef7.getAuthority();
        assertThat(authority7.toString()).isEqualTo("101.102.103.104");
        assertThat(authority7.getUserinfo()).isNull();
        var host7 = authority7.getHost();
        assertThat(host7.toString()).isEqualTo("101.102.103.104");
        assertThat(host7.getValue()).isEqualTo("101.102.103.104");
        assertThat(host7.getType()).isEqualTo(HostType.IPV4);
        assertThat(authority7.getPort()).isEqualTo(-1);
        assertThat(uriRef7.getPath()).isEqualTo("");
        assertThat(uriRef7.getQuery()).isNull();
        assertThat(uriRef7.getFragment()).isNull();

        var uriRef8 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]")).build();
        assertThat(uriRef8.toString()).isEqualTo("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
        assertThat(uriRef8.isRelativeReference()).isFalse();
        assertThat(uriRef8.hasAuthority()).isTrue();
        assertThat(uriRef8.getScheme()).isEqualTo("http");
        var authority8 = uriRef8.getAuthority();
        assertThat(authority8.toString()).isEqualTo("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
        assertThat(authority8.getUserinfo()).isNull();
        var host8 = authority8.getHost();
        assertThat(host8.toString()).isEqualTo("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
        assertThat(host8.getValue()).isEqualTo("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");
        assertThat(host8.getType()).isEqualTo(HostType.IPV6);
        assertThat(authority8.getPort()).isEqualTo(-1);
        assertThat(uriRef8.getPath()).isEqualTo("");
        assertThat(uriRef8.getQuery()).isNull();
        assertThat(uriRef8.getFragment()).isNull();

        var uriRef9 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://[2001:db8:0:1:1:1:1:1]")).build();
        assertThat(uriRef9.toString()).isEqualTo("http://[2001:db8:0:1:1:1:1:1]");
        assertThat(uriRef9.isRelativeReference()).isFalse();
        assertThat(uriRef9.hasAuthority()).isTrue();
        assertThat(uriRef9.getScheme()).isEqualTo("http");
        var authority9 = uriRef9.getAuthority();
        assertThat(authority9.toString()).isEqualTo("[2001:db8:0:1:1:1:1:1]");
        assertThat(authority9.getUserinfo()).isNull();
        var host9 = authority9.getHost();
        assertThat(host9.toString()).isEqualTo("[2001:db8:0:1:1:1:1:1]");
        assertThat(host9.getValue()).isEqualTo("[2001:db8:0:1:1:1:1:1]");
        assertThat(host9.getType()).isEqualTo(HostType.IPV6);
        assertThat(authority9.getPort()).isEqualTo(-1);
        assertThat(uriRef9.getPath()).isEqualTo("");
        assertThat(uriRef9.getQuery()).isNull();
        assertThat(uriRef9.getFragment()).isNull();

        var uriRef10 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://[2001:0:9d38:6abd:0:0:0:42]")).build();
        assertThat(uriRef10.toString()).isEqualTo("http://[2001:0:9d38:6abd:0:0:0:42]");
        assertThat(uriRef10.isRelativeReference()).isFalse();
        assertThat(uriRef10.hasAuthority()).isTrue();
        assertThat(uriRef10.getScheme()).isEqualTo("http");
        var authority10 = uriRef10.getAuthority();
        assertThat(authority10.toString()).isEqualTo("[2001:0:9d38:6abd:0:0:0:42]");
        assertThat(authority10.getUserinfo()).isNull();
        var host10 = authority10.getHost();
        assertThat(host10.toString()).isEqualTo("[2001:0:9d38:6abd:0:0:0:42]");
        assertThat(host10.getValue()).isEqualTo("[2001:0:9d38:6abd:0:0:0:42]");
        assertThat(host10.getType()).isEqualTo(HostType.IPV6);
        assertThat(authority10.getPort()).isEqualTo(-1);
        assertThat(uriRef10.getPath()).isEqualTo("");
        assertThat(uriRef10.getQuery()).isNull();
        assertThat(uriRef10.getFragment()).isNull();

        var uriRef11 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://[fe80::1]")).build();
        assertThat(uriRef11.toString()).isEqualTo("http://[fe80::1]");
        assertThat(uriRef11.isRelativeReference()).isFalse();
        assertThat(uriRef11.hasAuthority()).isTrue();
        assertThat(uriRef11.getScheme()).isEqualTo("http");
        var authority11 = uriRef11.getAuthority();
        assertThat(authority11.toString()).isEqualTo("[fe80::1]");
        assertThat(authority11.getUserinfo()).isNull();
        var host11 = authority11.getHost();
        assertThat(host11.toString()).isEqualTo("[fe80::1]");
        assertThat(host11.getValue()).isEqualTo("[fe80::1]");
        assertThat(host11.getType()).isEqualTo(HostType.IPV6);
        assertThat(authority11.getPort()).isEqualTo(-1);
        assertThat(uriRef11.getPath()).isEqualTo("");
        assertThat(uriRef11.getQuery()).isNull();
        assertThat(uriRef11.getFragment()).isNull();

        var uriRef12 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://[2001:0:3238:DFE1:63::FEFB]")).build();
        assertThat(uriRef12.toString()).isEqualTo("http://[2001:0:3238:DFE1:63::FEFB]");
        assertThat(uriRef12.isRelativeReference()).isFalse();
        assertThat(uriRef12.hasAuthority()).isTrue();
        assertThat(uriRef12.getScheme()).isEqualTo("http");
        var authority12 = uriRef12.getAuthority();
        assertThat(authority12.toString()).isEqualTo("[2001:0:3238:DFE1:63::FEFB]");
        assertThat(authority12.getUserinfo()).isNull();
        var host12 = authority12.getHost();
        assertThat(host12.toString()).isEqualTo("[2001:0:3238:DFE1:63::FEFB]");
        assertThat(host12.getValue()).isEqualTo("[2001:0:3238:DFE1:63::FEFB]");
        assertThat(host12.getType()).isEqualTo(HostType.IPV6);
        assertThat(authority12.getPort()).isEqualTo(-1);
        assertThat(uriRef12.getPath()).isEqualTo("");
        assertThat(uriRef12.getQuery()).isNull();
        assertThat(uriRef12.getFragment()).isNull();

        var uriRef13 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://[v1.fe80::a+en1]")).build();
        assertThat(uriRef13.toString()).isEqualTo("http://[v1.fe80::a+en1]");
        assertThat(uriRef13.isRelativeReference()).isFalse();
        assertThat(uriRef13.hasAuthority()).isTrue();
        assertThat(uriRef13.getScheme()).isEqualTo("http");
        var authority13 = uriRef13.getAuthority();
        assertThat(authority13.toString()).isEqualTo("[v1.fe80::a+en1]");
        assertThat(authority13.getUserinfo()).isNull();
        var host13 = authority13.getHost();
        assertThat(host13.toString()).isEqualTo("[v1.fe80::a+en1]");
        assertThat(host13.getValue()).isEqualTo("[v1.fe80::a+en1]");
        assertThat(host13.getType()).isEqualTo(HostType.IPVFUTURE);
        assertThat(authority13.getPort()).isEqualTo(-1);
        assertThat(uriRef13.getPath()).isEqualTo("");
        assertThat(uriRef13.getQuery()).isNull();
        assertThat(uriRef13.getFragment()).isNull();

        var uriRef14 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D")).build();
        assertThat(uriRef14.toString()).isEqualTo("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D");
        assertThat(uriRef14.isRelativeReference()).isFalse();
        assertThat(uriRef14.hasAuthority()).isTrue();
        assertThat(uriRef14.getScheme()).isEqualTo("http");
        var authority14 = uriRef14.getAuthority();
        assertThat(authority14.toString()).isEqualTo("%65%78%61%6D%70%6C%65%2E%63%6F%6D");
        assertThat(authority14.getUserinfo()).isNull();
        var host14 = authority14.getHost();
        assertThat(host14.toString()).isEqualTo("%65%78%61%6D%70%6C%65%2E%63%6F%6D");
        assertThat(host14.getValue()).isEqualTo("%65%78%61%6D%70%6C%65%2E%63%6F%6D");
        assertThat(host14.getType()).isEqualTo(REGNAME);
        assertThat(authority14.getPort()).isEqualTo(-1);
        assertThat(uriRef14.getPath()).isEqualTo("");
        assertThat(uriRef14.getQuery()).isNull();
        assertThat(uriRef14.getFragment()).isNull();

        var uriRef15 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://")).build();
        assertThat(uriRef15.isRelativeReference()).isFalse();
        assertThat(uriRef15.hasAuthority()).isTrue();
        assertThat(uriRef15.getScheme()).isEqualTo("http");
        var authority15 = uriRef15.getAuthority();
        assertThat(authority15.getUserinfo()).isNull();
        var host15 = authority15.getHost();
        assertThat(host15.getValue()).isEqualTo("");
        assertThat(host15.toString()).isEqualTo("");
        assertThat(host15.getType()).isEqualTo(REGNAME);
        assertThat(authority15.getPort()).isEqualTo(-1);
        assertThat(uriRef15.getPath()).isEqualTo("");
        assertThat(uriRef15.getQuery()).isNull();
        assertThat(uriRef15.getFragment()).isNull();

        var uriRef16 = URIReferenceBuilder.fromURIReference(URIReference.parse("http:///a")).build();
        assertThat(uriRef16.isRelativeReference()).isFalse();
        assertThat(uriRef16.hasAuthority()).isTrue();
        assertThat(uriRef16.getScheme()).isEqualTo("http");
        var authority16 = uriRef16.getAuthority();
        assertThat(authority16.getUserinfo()).isNull();
        var host16 = authority16.getHost();
        assertThat(host16.toString()).isEqualTo("");
        assertThat(host16.getValue()).isEqualTo("");
        assertThat(host16.getType()).isEqualTo(REGNAME);
        assertThat(authority16.getPort()).isEqualTo(-1);
        assertThat(uriRef16.getPath()).isEqualTo("/a");
        assertThat(uriRef16.getQuery()).isNull();
        assertThat(uriRef16.getFragment()).isNull();

        var uriRef17 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://example.com:80")).build();
        assertThat(uriRef17.isRelativeReference()).isFalse();
        assertThat(uriRef17.hasAuthority()).isTrue();
        assertThat(uriRef17.getScheme()).isEqualTo("http");
        var authority17 = uriRef17.getAuthority();
        assertThat(authority17.getUserinfo()).isNull();
        var host17 = authority17.getHost();
        assertThat(host17.toString()).isEqualTo("example.com");
        assertThat(host17.getValue()).isEqualTo("example.com");
        assertThat(host17.getType()).isEqualTo(REGNAME);
        assertThat(authority17.getPort()).isEqualTo(80);
        assertThat(uriRef17.getPath()).isEqualTo("");
        assertThat(uriRef17.getQuery()).isNull();
        assertThat(uriRef17.getFragment()).isNull();

        var uriRef18 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://example.com:")).build();
        assertThat(uriRef18.isRelativeReference()).isFalse();
        assertThat(uriRef18.hasAuthority()).isTrue();
        assertThat(uriRef18.getScheme()).isEqualTo("http");
        var authority18 = uriRef18.getAuthority();
        assertThat(authority18.getUserinfo()).isNull();
        var host18 = authority18.getHost();
        assertThat(host18.toString()).isEqualTo("example.com");
        assertThat(host18.getValue()).isEqualTo("example.com");
        assertThat(host18.getType()).isEqualTo(REGNAME);
        assertThat(authority18.getPort()).isEqualTo(-1);
        assertThat(uriRef18.getPath()).isEqualTo("");
        assertThat(uriRef18.getQuery()).isNull();
        assertThat(uriRef18.getFragment()).isNull();

        var uriRef19 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://example.com:001")).build();
        assertThat(uriRef19.isRelativeReference()).isFalse();
        assertThat(uriRef19.hasAuthority()).isTrue();
        assertThat(uriRef19.getScheme()).isEqualTo("http");
        var authority19 = uriRef19.getAuthority();
        assertThat(authority19.getUserinfo()).isNull();
        var host19 = authority19.getHost();
        assertThat(host19.toString()).isEqualTo("example.com");
        assertThat(host19.getValue()).isEqualTo("example.com");
        assertThat(host19.getType()).isEqualTo(REGNAME);
        assertThat(authority19.getPort()).isEqualTo(1);
        assertThat(uriRef19.getPath()).isEqualTo("");
        assertThat(uriRef19.getQuery()).isNull();
        assertThat(uriRef19.getFragment()).isNull();

        var uriRef20 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://example.com/a/b/c")).build();
        assertThat(uriRef20.toString()).isEqualTo("http://example.com/a/b/c");
        assertThat(uriRef20.isRelativeReference()).isFalse();
        assertThat(uriRef20.hasAuthority()).isTrue();
        assertThat(uriRef20.getScheme()).isEqualTo("http");
        var authority20 = uriRef20.getAuthority();
        assertThat(authority20.toString()).isEqualTo("example.com");
        assertThat(authority20.getUserinfo()).isNull();
        var host20 = authority20.getHost();
        assertThat(host20.toString()).isEqualTo("example.com");
        assertThat(host20.getValue()).isEqualTo("example.com");
        assertThat(host20.getType()).isEqualTo(REGNAME);
        assertThat(authority20.getPort()).isEqualTo(-1);
        assertThat(uriRef20.getPath()).isEqualTo("/a/b/c");
        assertThat(uriRef20.getQuery()).isNull();
        assertThat(uriRef20.getFragment()).isNull();

        var uriRef21 = URIReferenceBuilder.fromURIReference(URIReference.parse("http://example.com/%61/%62/%63")).build();
        assertThat(uriRef21.toString()).isEqualTo("http://example.com/%61/%62/%63");
        assertThat(uriRef21.isRelativeReference()).isFalse();
        assertThat(uriRef21.hasAuthority()).isTrue();
        assertThat(uriRef21.getScheme()).isEqualTo("http");
        var authority21 = uriRef21.getAuthority();
        assertThat(authority21.toString()).isEqualTo("example.com");
        assertThat(authority21.getUserinfo()).isNull();
        var host21 = authority21.getHost();
        assertThat(host21.toString()).isEqualTo("example.com");
        assertThat(host21.getValue()).isEqualTo("example.com");
        assertThat(host21.getType()).isEqualTo(REGNAME);
        assertThat(authority21.getPort()).isEqualTo(-1);
        assertThat(uriRef21.getPath()).isEqualTo("/%61/%62/%63");
        assertThat(uriRef21.getQuery()).isNull();
        assertThat(uriRef21.getFragment()).isNull();

        var uriRef22 = URIReferenceBuilder.fromURIReference(URIReference.parse("http:/a")).setAuthorityRequired(false).build();
        assertThat(uriRef22.isRelativeReference()).isFalse();
        assertThat(uriRef22.hasAuthority()).isFalse();
        assertThat(uriRef22.getScheme()).isEqualTo("http");
        assertThat(uriRef22.getAuthority()).isNull();
        assertThat(uriRef22.getPath()).isEqualTo("/a");
        assertThat(uriRef22.getQuery()).isNull();
        assertThat(uriRef22.getFragment()).isNull();

        var uriRef23 = URIReferenceBuilder.fromURIReference(URIReference.parse("http:a")).setAuthorityRequired(false).build();
        assertThat(uriRef23.isRelativeReference()).isFalse();
        assertThat(uriRef23.hasAuthority()).isFalse();
        assertThat(uriRef23.getScheme()).isEqualTo("http");
        assertThat(uriRef23.getAuthority()).isNull();
        assertThat(uriRef23.getPath()).isEqualTo("a");
        assertThat(uriRef23.getQuery()).isNull();
        assertThat(uriRef23.getFragment()).isNull();

        var uriRef24 = URIReferenceBuilder.fromURIReference(URIReference.parse("//")).build();
        assertThat(uriRef24.isRelativeReference()).isTrue();
        assertThat(uriRef24.hasAuthority()).isTrue();
        assertThat(uriRef24.getScheme()).isNull();
        var authority24 = uriRef24.getAuthority();
        assertThat(authority24.toString()).isEqualTo("");
        assertThat(authority24.getUserinfo()).isNull();
        var host24 = authority24.getHost();
        assertThat(host24.toString()).isEqualTo("");
        assertThat(host24.getValue()).isEqualTo("");
        assertThat(host24.getType()).isEqualTo(REGNAME);
        assertThat(authority24.getPort()).isEqualTo(-1);
        assertThat(uriRef24.getPath()).isEqualTo("");
        assertThat(uriRef24.getQuery()).isNull();
        assertThat(uriRef24.getFragment()).isNull();

        assertThrowsNPE(
            "The input string must not be null.",
            () -> URIReferenceBuilder.fromURIReference(URIReference.parse((String)null)).build());
    }


    @Test
    void set_authority_required()
    {
        var uriRef1 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setAuthorityRequired(true)
            .build();
        assertThat(uriRef1.toString()).isEqualTo("http://example.com");
        assertThat(uriRef1.isRelativeReference()).isFalse();
        assertThat(uriRef1.hasAuthority()).isTrue();
        assertThat(uriRef1.getScheme()).isEqualTo("http");
        var authority1 = uriRef1.getAuthority();
        assertThat(authority1.toString()).isEqualTo("example.com");
        assertThat(authority1.getUserinfo()).isNull();
        var host1 = authority1.getHost();
        assertThat(host1.toString()).isEqualTo("example.com");
        assertThat(host1.getValue()).isEqualTo("example.com");
        assertThat(host1.getType()).isEqualTo(REGNAME);
        assertThat(authority1.getPort()).isEqualTo(-1);
        assertThat(uriRef1.getPath()).isEqualTo("");
        assertThat(uriRef1.getQuery()).isNull();
        assertThat(uriRef1.getFragment()).isNull();

        var uriRef2 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setAuthorityRequired(false)
            .build();
        assertThat(uriRef2.toString()).isEqualTo("http:");
        assertThat(uriRef2.isRelativeReference()).isFalse();
        assertThat(uriRef2.hasAuthority()).isFalse();
        assertThat(uriRef2.getScheme()).isEqualTo("http");
        assertThat(uriRef2.getAuthority()).isNull();
        assertThat(uriRef1.getPath()).isEqualTo("");
        assertThat(uriRef1.getQuery()).isNull();
        assertThat(uriRef1.getFragment()).isNull();
    }


    @Test
    void set_scheme()
    {
        var uriRef1 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setScheme("ftp")
            .build();
        assertThat(uriRef1.getScheme()).isEqualTo("ftp");

        var uriRef2 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setScheme("https")
            .build();
        assertThat(uriRef2.getScheme()).isEqualTo("https");

        var uriRef3 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setScheme(null)
            .build();
        assertThat(uriRef3.getScheme()).isNull();
    }


    @Test
    void set_host()
    {
        var uriRef1 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setHost("example2.com")
            .build();
        assertThat(uriRef1.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef1.getHost().getValue()).isEqualTo("example2.com");

        var uriRef2 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setHost("101.102.103.104")
            .build();
        assertThat(uriRef2.getHost().getType()).isEqualTo(IPV4);
        assertThat(uriRef2.getHost().getValue()).isEqualTo("101.102.103.104");

        var uriRef3 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setHost("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]")
            .build();
        assertThat(uriRef3.getHost().getType()).isEqualTo(IPV6);
        assertThat(uriRef3.getHost().getValue()).isEqualTo("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]");

        var uriRef4 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setHost("[2001:db8:0:1:1:1:1:1]")
            .build();
        assertThat(uriRef4.getHost().getType()).isEqualTo(IPV6);
        assertThat(uriRef4.getHost().getValue()).isEqualTo("[2001:db8:0:1:1:1:1:1]");

        var uriRef5 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setHost("[2001:0:9d38:6abd:0:0:0:42]")
            .build();
        assertThat(uriRef5.getHost().getType()).isEqualTo(IPV6);
        assertThat(uriRef5.getHost().getValue()).isEqualTo("[2001:0:9d38:6abd:0:0:0:42]");

        var uriRef6 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setHost("[fe80::1]")
            .build();
        assertThat(uriRef6.getHost().getType()).isEqualTo(IPV6);
        assertThat(uriRef6.getHost().getValue()).isEqualTo("[fe80::1]");

        var uriRef7 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setHost("[2001:0:3238:DFE1:63::FEFB]")
            .build();
        assertThat(uriRef7.getHost().getType()).isEqualTo(IPV6);
        assertThat(uriRef7.getHost().getValue()).isEqualTo("[2001:0:3238:DFE1:63::FEFB]");

        var uriRef8 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setHost("[v1.fe80::a+en1]")
            .build();
        assertThat(uriRef8.getHost().getType()).isEqualTo(IPVFUTURE);
        assertThat(uriRef8.getHost().getValue()).isEqualTo("[v1.fe80::a+en1]");

        var uriRef9 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setHost("%65%78%61%6D%70%6C%65%2E%63%6F%6D")
            .build();
        assertThat(uriRef9.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef9.getHost().getValue()).isEqualTo("%65%78%61%6D%70%6C%65%2E%63%6F%6D");

        var uriRef10 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setHost("")
            .build();
        assertThat(uriRef10.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef10.getHost().getValue()).isEqualTo("");

        var uriRef11 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setHost(null)
            .build();
        assertThat(uriRef11.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef11.getHost().getValue()).isNull();
    }


    @Test
    void set_path()
    {
        var uriRef1 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setPath("/a")
            .build();
        assertThat(uriRef1.getPath()).isEqualTo("/a");

        var uriRef2 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setPath("/a/b")
            .build();
        assertThat(uriRef2.getPath()).isEqualTo("/a/b");

        var uriRef3 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setPath("/")
            .build();
        assertThat(uriRef3.getPath()).isEqualTo("/");

        var uriRef4 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setPath("")
            .build();
        assertThat(uriRef4.getPath()).isEqualTo("");

        var uriRef5 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .setPath(null)
            .build();
        assertThat(uriRef5.getPath()).isNull();
    }


    @Test
    void set_path_segments()
    {
        var uriRef1 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .appendPathSegments("a", "b", "c")
            .build();
        assertThat(uriRef1.getPath()).isEqualTo("/a/b/c");

        var uriRef2 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .appendPathSegments("")
            .build();
        assertThat(uriRef2.getPath()).isEqualTo("/");

        assertThrowsNPE("A segment must not be null.", () -> URIReferenceBuilder
            .fromURIReference("http://example.com")
            .appendPathSegments((String)null)
            .build());
    }


    @Test
    void append_query_param()
    {
        var uriRef1 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .appendQueryParam("k", "v")
            .build();
        assertThat(uriRef1.getQuery()).isEqualTo("k=v");

        var uriRef2 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .appendQueryParam("k1", "v1")
            .appendQueryParam("k2", "v2")
            .build();
        assertThat(uriRef2.getQuery()).isEqualTo("k1=v1&k2=v2");

        var uriRef3 = URIReferenceBuilder
            .fromURIReference("http://example.com")
            .appendQueryParam("", "")
            .build();
        assertThat(uriRef3.getQuery()).isEqualTo("=");

        assertThrowsNPE("The key must not be null.", () -> URIReferenceBuilder
            .fromURIReference("http://example.com")
            .appendQueryParam(null, null));
    }


    @Test
    void replace_query_param()
    {
        var uriRef1 = URIReferenceBuilder
            .fromURIReference("http://example.com?k=v")
            .replaceQueryParam("k", "w")
            .build();
        assertThat(uriRef1.getQuery()).isEqualTo("k=w");

        var uriRef2 = URIReferenceBuilder
            .fromURIReference("http://example.com?k=v")
            .replaceQueryParam("k", null)
            .build();
        assertThat(uriRef2.getQuery()).isEqualTo("k");

        assertThrowsNPE("The key must not be null.", () -> URIReferenceBuilder
            .fromURIReference("http://example.com?k=v")
            .appendQueryParam(null, "w"));
    }


    @Test
    void remove_query_param()
    {
        var uriRef1 = URIReferenceBuilder
            .fromURIReference("http://example.com?k=v")
            .removeQueryParam("k")
            .build();
        assertThat(uriRef1.getQuery()).isNull();

        var uriRef2 = URIReferenceBuilder
            .fromURIReference("http://example.com?k=v")
            .removeQueryParam(null)
            .build();
        assertThat(uriRef2.getQuery()).isEqualTo("k=v");
    }


    @Test
    void set_query()
    {
        var uriRef1 = URIReferenceBuilder.fromURIReference("http://example.com").setQuery("k=v").build();
        assertThat(uriRef1.getQuery()).isEqualTo("k=v");

        var uriRef2 = URIReferenceBuilder.fromURIReference("http://example.com").setQuery("k=").build();
        assertThat(uriRef2.getQuery()).isEqualTo("k=");

        var uriRef3 = URIReferenceBuilder.fromURIReference("http://example.com").setQuery("k").build();
        assertThat(uriRef3.getQuery()).isEqualTo("k");

        var uriRef4 = URIReferenceBuilder.fromURIReference("http://example.com").setQuery("").build();
        assertThat(uriRef4.getQuery()).isEqualTo("");

        var uriRef5 = URIReferenceBuilder.fromURIReference("http://example.com").setQuery(null).build();
        assertThat(uriRef5.getQuery()).isNull();
    }


    @Test
    void set_fragment()
    {
        var uriRef1 = URIReferenceBuilder.fromURIReference("http://example.com").setFragment("section1").build();
        assertThat(uriRef1.getFragment()).isEqualTo("section1");

        var uriRef2 = URIReferenceBuilder.fromURIReference("http://example.com").setFragment("fig%20A").build();
        assertThat(uriRef2.getFragment()).isEqualTo("fig%20A");

        var uriRef3 = URIReferenceBuilder.fromURIReference("http://example.com").setFragment("2.3").build();
        assertThat(uriRef3.getFragment()).isEqualTo("2.3");

        var uriRef4 = URIReferenceBuilder.fromURIReference("http://example.com").setFragment("").build();
        assertThat(uriRef4.getFragment()).isEqualTo("");

        var uriRef5 = URIReferenceBuilder.fromURIReference("http://example.com").setFragment(null).build();
        assertThat(uriRef5.getFragment()).isNull();
    }
}
