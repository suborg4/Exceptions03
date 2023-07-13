package task02;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Вы разрабатываете систему банковских транзакций.
 * Необходимо написать программу, которая позволяет пользователям осуществлять переводы средств
 * со своего банковского счета на другие счета. Однако, в системе существуют некоторые ограничения и возможные ошибки,
 * которые нужно обрабатывать.
 *
 * При переводе средств, сумма должна быть положительной и не превышать доступный баланс на счете.
 *
 * Если сумма перевода отрицательная или равна нулю,
 * выбрасывается исключение InvalidAmountException с сообщением "Некорректная сумма перевода".
 *
 * Если на балансе недостаточно средств для перевода,
 * выбрасывается исключение InsufficientFundsException с сообщением "Недостаточно средств на счете".
 *
 * При успешном переводе, сумма должна списываться с текущего счета и зачисляться на целевой счет.
 */

public class Transaction {
    public static void main(String[] args) {
        Client client01 = new Client("<Рокфеллер>");
        Client client02 = new Client("Ротшильд");
        try {
            Transaction transaction = new Transaction();
            transaction.addMoney(client01, transaction.inputFromUser(
                    String.format("Введите сумму для зачисления клиенту %s: ", client01.getName())));
            transaction.addMoney(client02, transaction.inputFromUser(
                    String.format("Введите сумму для зачисления клиенту %s: ", client02.getName())));
            transaction.transferMoney(client02, client01, transaction.inputFromUser(String.format(
                    "Введите сумму списания от клиента %s к клиенту %s: ", client02.getName(), client01.getName())));
            System.out.println("Транзакция успешно проведена");
            System.out.printf("Клиент %s, баланс: %.2f\n", client01.getName(), client01.getBalance());
            System.out.printf("Клиент %s, баланс: %.2f\n", client02.getName(), client02.getBalance());
        } catch (InvalidAmountException | InsufficientFundsException e){
            System.out.println("Транзакция отменена! Ошибка: " + e.getMessage());
        } catch (InputMismatchException e){
            System.out.println("Транзакция отменена! Ошибка: Некорректный ввод");
        }
    }

    public void addMoney(Client client, BigDecimal sumTransaction){
        BigDecimal balance = client.getBalance();
        client.setBalance(balance.add(sumTransaction));
    }

    public void takeMoney(Client client, BigDecimal sumTransaction) throws InsufficientFundsException{
        BigDecimal balance = client.getBalance();
        if (balance.subtract(sumTransaction).compareTo(new BigDecimal("0.0")) < 0){
            throw new InsufficientFundsException("Недостаточно денежных средств");
        }
        client.setBalance(balance.subtract(sumTransaction));
    }

    public void transferMoney(Client clientOut, Client clientIn, BigDecimal sumTransaction) throws InsufficientFundsException{
        takeMoney(clientOut, sumTransaction);
        addMoney(clientIn, sumTransaction);
    }

    public BigDecimal inputFromUser(String message) throws InvalidAmountException, InputMismatchException {
        Scanner sc = new Scanner(System.in);
        System.out.print(message);
        BigDecimal userInput = BigDecimal.valueOf(sc.nextDouble());
        if (userInput.compareTo(new BigDecimal("0.0")) == 0 || userInput.compareTo(new BigDecimal("0.0")) < 0){
            throw new InvalidAmountException("Некорректная сумма перевода");
        }
        return userInput;
    }
}

class InvalidAmountException extends Exception{
    public InvalidAmountException(String message) {
        super(message);
    }
}

class InsufficientFundsException extends Exception{
    public InsufficientFundsException(String message) {
        super(message);
    }
}