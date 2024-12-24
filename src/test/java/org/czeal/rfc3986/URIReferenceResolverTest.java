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
import static org.czeal.rfc3986.TestUtils.assertThrowsISE;
import static org.czeal.rfc3986.TestUtils.assertThrowsNPE;
import org.junit.jupiter.api.Test;


class URIReferenceResolverTest
{
    @Test
    void resolve()
    {
        var uriRef1 = new URIReferenceResolver().resolve(
            URIReference.parse("g:h", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef2 = new URIReferenceResolver().resolve(
            URIReference.parse("g", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef3 = new URIReferenceResolver().resolve(
            URIReference.parse("./g", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef4 = new URIReferenceResolver().resolve(
            URIReference.parse("g/", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef5 = new URIReferenceResolver().resolve(
            URIReference.parse("/g", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef6 = new URIReferenceResolver().resolve(
            URIReference.parse("//g", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef7 = new URIReferenceResolver().resolve(
            URIReference.parse("?y", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef8 = new URIReferenceResolver().resolve(
            URIReference.parse("g?y", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef9 = new URIReferenceResolver().resolve(
            URIReference.parse("#s", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef10 = new URIReferenceResolver().resolve(
            URIReference.parse("g#s", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef11 = new URIReferenceResolver().resolve(
            URIReference.parse("g?y#s", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef12 = new URIReferenceResolver().resolve(
            URIReference.parse(";x", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef13 = new URIReferenceResolver().resolve(
            URIReference.parse("g;x", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef14 = new URIReferenceResolver().resolve(
            URIReference.parse("g;x?y#s", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef15 = new URIReferenceResolver().resolve(
            URIReference.parse("", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef16 = new URIReferenceResolver().resolve(
            URIReference.parse(".", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef17 = new URIReferenceResolver().resolve(
            URIReference.parse("./", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef18 = new URIReferenceResolver().resolve(
            URIReference.parse("..", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef19 = new URIReferenceResolver().resolve(
            URIReference.parse("../", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef20 = new URIReferenceResolver().resolve(
            URIReference.parse("../g", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef21 = new URIReferenceResolver().resolve(
            URIReference.parse("../..", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef22 = new URIReferenceResolver().resolve(
            URIReference.parse("../../", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef23 = new URIReferenceResolver().resolve(
            URIReference.parse("../../g", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef24 = new URIReferenceResolver().resolve(
            URIReference.parse("g?y/./x", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef25 = new URIReferenceResolver().resolve(
            URIReference.parse("g?y/../x", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef26 = new URIReferenceResolver().resolve(
            URIReference.parse("g#s/./x", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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

        var uriRef27 = new URIReferenceResolver().resolve(
            URIReference.parse("g#s/../x", UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8));
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
            () -> new URIReferenceResolver().resolve(
                URIReference.parse(null, UTF_8), URIReference.parse("http://a/b/c/d;p?q", UTF_8)));

        assertThrowsISE(
            "The base URI must have a scheme.",
            () -> new URIReferenceResolver().resolve(
                URIReference.parse("g", UTF_8), URIReference.parse("/a/b/c/d;p?q", UTF_8)));

        assertThrowsISE(
            "The base URI must not have a fragment.",
            () -> new URIReferenceResolver().resolve(
                URIReference.parse("g", UTF_8), URIReference.parse("http://a/b/c/d;p?q#s", UTF_8)));
    }
}
