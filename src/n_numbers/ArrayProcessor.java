package n_numbers;

import java.util.List;
import java.util.concurrent.Callable;

class ArrayProcessor implements Callable<Integer> {
    private final List<Integer> part;

    public ArrayProcessor(List<Integer> part) {
        this.part = part;
    }

    @Override
    public Integer call() {
        int sum = 0;
        for (int num : part) {
            sum += num;
        }
        return sum;
    }
}
