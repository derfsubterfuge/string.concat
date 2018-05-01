/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package perf.benchmark;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Warmup(iterations = 3, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Thread)
public class TenStringConcatenationBenchmark {
    private String s0;
    private String s1;
    private String s2;
    private String s3;
    private String s4;
    private String s5;
    private String s6;
    private String s7;
    private String s8;
    private String s9;
    
    @Setup
    public void setupTest(){
        s0 = UUID.randomUUID().toString();
        s1 = UUID.randomUUID().toString();
        s2 = UUID.randomUUID().toString();
        s3 = UUID.randomUUID().toString();
        s4 = UUID.randomUUID().toString();
        s5 = UUID.randomUUID().toString();
        s6 = UUID.randomUUID().toString();
        s7 = UUID.randomUUID().toString();
        s8 = UUID.randomUUID().toString();
        s9 = UUID.randomUUID().toString();
    }

    @Benchmark
    public void testStringFormat(Blackhole bh) {
        String combined = String.format("%s%s%s%s%s%s%s%s%s%s", s0, s1, s2, s3, s4, s5, s6, s7, s8, s9);
        bh.consume(combined);
    }

    @Benchmark
    public void testPlus(Blackhole bh) {
        String combined = s0
                + s1 
                + s2 
                + s3 
                + s4 
                + s5 
                + s6 
                + s7 
                + s8 
                + s9;
        bh.consume(combined);
    }

    @Benchmark
    public void testStringBuilder(Blackhole bh) {
        StringBuilder sb = new StringBuilder()
                .append(s0)
                .append(s1)
                .append(s2)
                .append(s3)
                .append(s4)
                .append(s5)
                .append(s6)
                .append(s7)
                .append(s9);
        bh.consume(sb.toString());
    }

    @Benchmark
    public void testStringBuffer(Blackhole bh) {
        StringBuffer sb = new StringBuffer()
                .append(s0)
                .append(s1)
                .append(s2)
                .append(s3)
                .append(s4)
                .append(s5)
                .append(s6)
                .append(s7)
                .append(s9);
        bh.consume(sb.toString());
    }

    @Benchmark
    public void testStringJoin(Blackhole bh) {
        String combined = String.join("", s0, s1, s2, s3, s4, s5, s6, s7, s8, s9);
        bh.consume(combined);
    }

    @Benchmark
    public void testStringConcat(Blackhole bh) {
        String combined = s0.concat(s1)
                .concat(s2)
                .concat(s3)
                .concat(s4)
                .concat(s5)
                .concat(s6)
                .concat(s7)
                .concat(s9);
        bh.consume(combined);
    }

    @Benchmark
    public void testStringUtilsJoin(Blackhole bh) {
        String combined = StringUtils.join(s0, s1, s2, s3, s4, s5, s6, s7, s8, s9);
        bh.consume(combined);
    }

    @Benchmark
    public void testGuavaJoiner(Blackhole bh) {
        String combined = Joiner.on("").join(s0, s1, s2, s3, s4, s5, s6, s7, s8, s9);
        bh.consume(combined);
    }

    @Benchmark
    public void testStreamJoining(Blackhole bh) {
        String combined = Arrays.asList(s0, s1, s2, s3, s4, s5, s6, s7, s8, s9)
                .stream()
                .collect(Collectors.joining());
        bh.consume(combined);
    }
}
