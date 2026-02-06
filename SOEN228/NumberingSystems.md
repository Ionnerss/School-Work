
# Positional Numbering Systems

## Examples

### Binary
- **11001₂ in powers of 2:**
    - $1 \times 2^0 + 0 \times 2^1 + 0 \times 2^2 + 1 \times 2^3 + 1 \times 2^4$
    - $= 1 + 8 + 16 = 25$
    - *(Multiply from right to left)*

- **110.011₂ in powers of 2:**
    - $0 \times 2^0 + 1 \times 2^1 + 1 \times 2^2 + 0 \times 2^{-1} + 1 \times 2^{-2} + 1 \times 2^{-3}$
    - $= 2 + 4 + 0.25 + 0.125 = 6.375$

### Hexadecimal
- **Base 16 digits:**
    - $\{0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F\}$

- **AB1₁₆ in powers of 16:**
    - $1 \times 16^0 + 11 \times 16^1 + 10 \times 16^2$
    - $= 1 + 176 + 2560 = 2737$

- **2D.0F₁₆ in powers of 16:**
    - $13 \times 16^0 + 2 \times 16^1 + 0 \times 16^{-1} + 15 \times 16^{-2}$
    - $= 13 + 32 + 0 + 0.0586 = 45.0586$

---

## Converting Between Bases (Integers)

### Decimal to Binary (Successive Divisions)

- **18 to binary:**
    - $18 \div 2 = 9$ remainder $0$
    - $9 \div 2 = 4$ remainder $1$
    - $4 \div 2 = 2$ remainder $0$
    - $2 \div 2 = 1$ remainder $0$
    - $1 \div 2 = 0$ remainder $1$
    - **Result:** $10010_2$

- **31 to binary:**
    - $31 \div 2 = 15$ remainder $1$
    - $15 \div 2 = 7$ remainder $1$
    - $7 \div 2 = 3$ remainder $1$
    - $3 \div 2 = 1$ remainder $1$
    - $1 \div 2 = 0$ remainder $1$
    - **Result:** $11111_2$

-------------------------------------
# Binary Arithmetic: Unsigned Numbers

- Arithmetic operations similar to decimal
- Example:
    - 1001 + 0011 = 1100
    - (Example with overflow) 1001 + 0111 = 10000
    - (substraction) 1001 - 0111 = 0010
    - (multiplication) 1001 x 0111 = 1001 + 10010 + 100100+ 000000 = 01111111
    - (division) 1101/10 = 110
