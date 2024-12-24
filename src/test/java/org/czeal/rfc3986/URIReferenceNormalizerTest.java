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
import static org.czeal.rfc3986.HostType.REGNAME;
import static org.czeal.rfc3986.TestUtils.assertThrowsISE;

import org.junit.jupiter.api.Test;


class URIReferenceNormalizerTest
{
    @Test
    void normalize()
    {
        var uriRef1 = new URIReferenceNormalizer().normalize(URIReference.parse("hTTp://example.com/", UTF_8));
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

        var uriRef2 = new URIReferenceNormalizer().normalize(URIReference.parse("http://example.com/", UTF_8));
        assertThat(uriRef2.hasAuthority()).isTrue();
        assertThat(uriRef2.getAuthority().toString()).isEqualTo("example.com");
        assertThat(uriRef2.getUserinfo()).isNull();
        assertThat(uriRef2.getHost().toString()).isEqualTo("example.com");
        assertThat(uriRef2.getHost().getValue()).isEqualTo("example.com");
        assertThat(uriRef2.getHost().getType()).isEqualTo(REGNAME);
        assertThat(uriRef2.getPort()).isEqualTo(-1);
        assertThat(uriRef2.getQuery()).isNull();
        assertThat(uriRef2.getFragment()).isNull();

        var uriRef3 = new URIReferenceNormalizer().normalize(URIReference.parse("http://%75ser@example.com/", UTF_8));
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

        var uriRef4 = new URIReferenceNormalizer().normalize(URIReference.parse("http://%e3%83%a6%e3%83%bc%e3%82%b6%e3%83%bc@example.com/", UTF_8));
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

        var uriRef5 = new URIReferenceNormalizer().normalize(URIReference.parse("http://%65%78%61%6D%70%6C%65.com/", UTF_8));
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

        var uriRef6 = new URIReferenceNormalizer().normalize(URIReference.parse("http://%e4%be%8b.com/", UTF_8));
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

        var uriRef7 = new URIReferenceNormalizer().normalize(URIReference.parse("http://LOCALhost/", UTF_8));
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

        var uriRef8 = new URIReferenceNormalizer().normalize(URIReference.parse("http://example.com", UTF_8));
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

        var uriRef9 = new URIReferenceNormalizer().normalize(URIReference.parse("http://example.com/%61/%62/%63/", UTF_8));
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

        var uriRef10 = new URIReferenceNormalizer().normalize(URIReference.parse("http://example.com/%e3%83%91%e3%82%b9/", UTF_8));
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

        var uriRef11 = new URIReferenceNormalizer().normalize(URIReference.parse("http://example.com/a/b/c/../d/", UTF_8));
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

        var uriRef12 = new URIReferenceNormalizer().normalize(URIReference.parse("http://example.com:80/", UTF_8));
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
            () -> new URIReferenceNormalizer().normalize(URIReference.parse("//example.com", UTF_8)));
    }
}
