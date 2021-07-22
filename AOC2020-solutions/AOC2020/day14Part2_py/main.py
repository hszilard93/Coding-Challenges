import re

# Challenge at https://adventofcode.com/2020/day/14#part2

def gen_addr_code(mask: str, addr: int) -> str:
    addr_str = ''
    while len(addr_str) != 36:
        addr_str = str(addr % 2) + addr_str
        addr = addr >> 1
    addr_str_ = ''
    for i in range(0, len(mask)):
        addr_str_ += mask[i] if mask[i] in {'1', 'X'} else addr_str[i]
    # print(addr_str_)
    return addr_str_


def all_addr(addr_code: str, addr=0) -> set:
    # X1101X
    acc = set()
    for i in range(0, len(addr_code)):
        pwr = len(addr_code) - i - 1
        if addr_code[i] == '1':
            addr += 2 ** pwr
        elif addr_code[i] == 'X':
            acc = acc.union(all_addr('0' + addr_code[i + 1:], addr))
            acc = acc.union(all_addr('1' + addr_code[i + 1:], addr))
            return acc

    acc.add(addr)
    return acc


file = open('in.txt', 'r')
mask = ''
memory = {}
for l in file.readlines():
    spl = str.split(l.strip(), ' ')
    if spl[0] == 'mask':
        mask = spl[-1]
    else:
        addr = int(re.search('''^mem\[(\d*)\]''', spl[0]).group(1))
        val = int(spl[-1])
        addr_code = gen_addr_code(mask, addr)
        addresses = all_addr(addr_code)
        # print(addresses)
        for a in addresses:
            memory[a] = val

sum = 0
for a, v in memory.items():
    sum += v
print(sum)
