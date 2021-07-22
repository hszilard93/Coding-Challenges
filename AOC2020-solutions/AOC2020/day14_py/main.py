import re

# Challenge at https://adventofcode.com/2020/day/14

def masked(mask: str, val: int) -> int:
    if len(mask) != 36:
        print("ERROR! Mask", mask, "is illegal.")
        return -1
    pwr = 35
    for c in mask:
        shft_val = val >> pwr
        if c == '1':
            if shft_val % 2 == 0:
                val += 2**pwr
        elif c == '0':
            if shft_val % 2 == 1:
                val -= 2**pwr
        pwr -= 1
    return val


file = open('in.txt', 'r')
mask = ''
memory = {}
for l in file.readlines():
    spl = str.split(l.strip(), ' ')
    if spl[0] == 'mask':
        mask = spl[-1]
    else:
        addr = re.search('''^mem\[(\d*)\]''', spl[0]).group(1)
        val = int(spl[-1])
        memory[addr] = masked(mask, val)

sum = 0
for a, v in memory.items():
    sum += v

print(sum)
