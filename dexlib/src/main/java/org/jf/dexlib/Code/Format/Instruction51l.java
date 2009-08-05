/*
 * [The "BSD licence"]
 * Copyright (c) 2009 Ben Gruver
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jf.dexlib.Code.Format;

import org.jf.dexlib.Code.Instruction;
import org.jf.dexlib.Code.Opcode;
import org.jf.dexlib.DexFile;
import org.jf.dexlib.Util.NumberUtils;

public class Instruction51l extends Instruction {
    public static final Instruction.InstructionFactory Factory = new Factory();

    public Instruction51l(Opcode opcode, short regA, long litB) {
        super(opcode);

        if (regA >= 1 << 8) {
            throw new RuntimeException("The register number must be less than v256");
        }

        buffer[0] = opcode.value;
        buffer[1] = (byte) regA;
        buffer[2] = (byte) litB;
        buffer[3] = (byte) (litB >> 8);
        buffer[4] = (byte) (litB >> 16);
        buffer[5] = (byte) (litB >> 24);
        buffer[6] = (byte) (litB >> 32);
        buffer[7] = (byte) (litB >> 40);
        buffer[8] = (byte) (litB >> 48);
        buffer[9] = (byte) (litB >> 56);
    }

    private Instruction51l(Opcode opcode, byte[] buffer, int bufferIndex) {
        super(opcode, buffer, bufferIndex);
    }

    public Format getFormat() {
        return Format.Format51l;
    }

    public short getRegister() {
        return NumberUtils.decodeUnsignedByte(buffer[bufferIndex + 1]);
    }

    public long getLiteral() {
        return NumberUtils.decodeLong(buffer, bufferIndex + 2);
    }

    private static class Factory implements Instruction.InstructionFactory {
        public Instruction makeInstruction(DexFile dexFile, Opcode opcode, byte[] buffer, int bufferIndex) {
            return new Instruction51l(opcode, buffer, bufferIndex);
        }
    }
}
